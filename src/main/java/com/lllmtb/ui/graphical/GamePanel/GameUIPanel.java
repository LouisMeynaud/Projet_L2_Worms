package com.lllmtb.ui.graphical.GamePanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.turn.Turn;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.turn.ChangeWormsEvent;
import com.lllmtb.events.turn.TurnEndEvent;
import com.lllmtb.events.game.GamePauseEvent;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.events.game.SaveEvent;
import com.lllmtb.events.game.ToolSelectedEvent;
import com.lllmtb.events.game.WeaponSelectedEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.events.turn.TurnSkipEvent;
import com.lllmtb.events.ui.graphical.NotificationEvent;
import com.lllmtb.events.ui.graphical.RequestFocus;
import com.lllmtb.events.ui.graphical.ScreenHomeEvent;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.item.inventory.Inventory;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.model.shop.ShopElement;
import com.lllmtb.model.shop.ShopManager;
import com.lllmtb.physics.Vector;
import com.lllmtb.ui.graphical.Utilitaire.SoundPlayer;
import com.lllmtb.ui.graphical.Utilitaire.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire.Utilitaire;

/**
 * panneau principal de l'interface utilisateur du jeu
 * </p>
 * ce panneau se superpose au dessins du jeu et contient les bouton comme inventaire pose etc
 * ainsi que les les panneau d'inventaire de menu pose etc
 * il utilise un cardLayout afin d'afficher les bons élement.
 */
public class GameUIPanel extends JPanel {

    private final EventBus eventBus; // le system bus
    private JPanel pauseMenucontainer; //le contner pour le menu pause
    private JPanel inventoryContainer; // le contner pour le menu inventaire
    private GameWorld world; // le monde
    private CardLayout cardLayout; // le cardLayout qui contient les differente page
    private JPanel centerContainer; // le panneau global utilisans le cardLayout
    private JPanel weaponGrid; // la grille d'arme dans l'inventaire
    private JPanel toolGrid; // la grille d'outil dans l'inventaire
    private JLabel timerLabel; // le label du timer
    private JLabel moneyLabel; // le label de l'argent
    private Timer uiRefreshTimer; //timer swing pour raffraichir le timer
    private JPanel timerContainer; // le contneur de couleur pour le timer


    private JPanel shopContainer;
    private JPanel shopWeaponGrid;
    private JPanel shopAmmoGrid;
    private JPanel shopToolGrid;
    private ShopManager shopManager;
    private JButton pauseButton;
    private JButton inventoryButton;
    private JButton skipTurnButton;
    private JButton shopButton;
    private int currentButtonSize = 80;
    private JButton changeWormsButton;

    private class WindPanel extends JPanel {
        public WindPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(85, 95));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            if (world == null || world.getPhysics() == null) return;

            int width = getWidth();
            int height = getHeight();

            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRoundRect(5, 5, width - 10, height - 10, 15, 15);
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRoundRect(5, 5, width - 10, height - 10, 15, 15);


            int centerX = width / 2;
            int centerY = height / 2 - 5;
            int radius = 22;
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);


            Vector wind = null;
            if (world.getPhysics().hasWind()) wind = world.getPhysics().getWind();
            double force = 0;
            double angle = 0;
            if (wind != null) {
                force = Math.sqrt(wind.getX() * wind.getX() + wind.getY() * wind.getY());
                angle = Math.atan2(wind.getY(), wind.getX());
            }


            g2d.setColor(Color.RED);
            if (force > 0.0001) {
                int endX = centerX + (int) (Math.cos(angle) * (radius * 0.9));
                int endY = centerY + (int) (Math.sin(angle) * (radius * 0.9));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(centerX, centerY, endX, endY);
            } else {
                g2d.fillOval(centerX - 3, centerY - 3, 6, 6);
            }

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));

            int vitesse = (int) (force / 1000);
            String windText = vitesse + " m/s";

            g2d.drawString(windText, 20, height - 15);
        }
    }

    /**
     * constructeur : initialise la disposition , les conteneurs , le HUD et les listenneur.
     * @param eventBus le system bus
     */
    public GameUIPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setReshowDelay(0);
        setOpaque(false);
        setLayout(new BorderLayout());
        setupCenterContainer();
        createPauseMenu();
        createInventoryContainer();
        createShopContainer();
        setupHUD();
        subscribeToSystemEvents();
        this.uiRefreshTimer = new Timer(100, e -> {
            updateTimerDisplay();
            this.repaint();
        });
        this.uiRefreshTimer.start();


        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeUI();
            }
        });
    }

    /**
     * configure le conteneur central avec un cardlayout avec un panel vide qui correspond a quand aucun menu est ouvert.
     */
    private void setupCenterContainer() {
        this.cardLayout = new CardLayout();
        this.centerContainer = new JPanel(cardLayout);
        this.centerContainer.setOpaque(false);

        //empty panel for view the game if there are not pannel selected
        JPanel emptyCard = new JPanel();
        emptyCard.setOpaque(false);
        this.centerContainer.add(emptyCard, "EMPTY");
        this.add(this.centerContainer, BorderLayout.CENTER);
    }

    /**
     * crée le HUD : avec une barre transparente inferieur et superieur pour y placer les bouton.
     * crée les bouton (Pause,Inventaire,Tir,Passer tour , ...)
     */
    private void setupHUD() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(10, 10, 0, 10));

        JPanel lowerBar = new JPanel(new BorderLayout());
        lowerBar.setOpaque(false);

        JPanel buttonsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsContainer.setOpaque(false);

        pauseButton = Utilitaire.createImageButton(TextureManager.UI, "Pause", 128, 128, 128, 128, "", "Arial", 16, Color.WHITE);
        inventoryButton = Utilitaire.createImageButton(TextureManager.UI, "InventoryButton", 128, 128, 128, 128, "", "Arial", 16, Color.WHITE);
        shopButton = Utilitaire.createImageButton(TextureManager.UI, "ShopButton", 128, 128, 128, 128, "", "Arial", 12, Color.YELLOW);
        skipTurnButton = Utilitaire.createImageButton(TextureManager.UI, "SkipTurn", 128, 128, 128, 128, "", "Arial", 16, Color.WHITE);
        changeWormsButton = Utilitaire.createImageButton(TextureManager.UI, "ChangeWorms", 128, 128, 128, 128, "", "Arial", 16, Color.WHITE);


        pauseButton.addActionListener(e -> {
            if (world != null) pauseMenu(true); });
        inventoryButton.addActionListener(e -> {
            if (world != null && !world.getPlayingTeam().isBot()) openCloseInventory(); });
        shopButton.addActionListener(e -> {
            if (world != null && !world.getPlayingTeam().isBot()) openCloseShop(); });
        skipTurnButton.addActionListener(e -> {
            if (world != null && !world.getPlayingTeam().isBot()) world.getBus().postQueued(new TurnSkipEvent()); });
        changeWormsButton.addActionListener(e -> {
            if (world != null && !world.getPlayingTeam().isBot()) world.getBus().postQueued(new ChangeWormsEvent()); });


        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(pauseButton);
        buttonsPanel.add(skipTurnButton);
        buttonsPanel.add(changeWormsButton);

        this.timerLabel = createTimerLabel();
        this.timerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g);
            }
        };
        this.timerContainer.setOpaque(false);
        this.timerContainer.setBorder(new EmptyBorder(5, 20, 5, 20));
        this.timerContainer.add(this.timerLabel);


        JPanel timerContainerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        timerContainerContainer.setOpaque(false);
        timerContainerContainer.setBorder(new EmptyBorder(15, 0, 0, 0));
        timerContainerContainer.add(this.timerContainer);

        JPanel moneyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        moneyPanel.setOpaque(false);

        this.moneyLabel = new JLabel("0 $");
        this.moneyLabel.setFont(new Font("Arial", Font.BOLD, 22));
        this.moneyLabel.setForeground(Color.YELLOW);
        BufferedImage img = TextureManager.UI.get("Coin");
        this.moneyLabel.setIcon(new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        moneyPanel.add(this.moneyLabel);
        topBar.add(buttonsPanel, BorderLayout.WEST);
        topBar.add(timerContainerContainer, BorderLayout.CENTER);
        topBar.add(moneyPanel, BorderLayout.EAST);

        buttonsContainer.add(inventoryButton);
        buttonsContainer.add(shopButton);
        lowerBar.add(buttonsContainer, BorderLayout.WEST);
        lowerBar.add(new WindPanel(), BorderLayout.EAST);


        this.add(topBar, BorderLayout.NORTH);
        this.add(lowerBar, BorderLayout.SOUTH);
    }

    /**
     * s'abonne au evenement utile a la classe.
     */
    private void subscribeToSystemEvents() {

        eventBus.subscribe(WorldCreatedEvent.class, event -> {
            this.world = event.world();
            this.shopManager = world.getShopManager();
            this.world.getBus().subscribe(NotificationEvent.class, Popup::add);
            this.world.getBus().subscribe(TurnEndEvent.class, e -> cardLayout.show(centerContainer, "EMPTY"));
            updateTimerDisplay();
        });

        eventBus.subscribe(GameStateUpdatedEvent.class, event -> {
            updateTimerDisplay();
            if (this.shopContainer != null && this.shopContainer.isVisible()) {
                refreshShopGrid();
            }
        });
    }

    ////////////Timer/////////////////

    /**
     * met a jour l'affichage du timer (texte et couleur).
     */
    private void updateTimerDisplay() {
        if (this.world == null) return;
        Team playingTeam = this.world.getPlayingTeam();
        if (playingTeam == null) {
            this.timerLabel.setText("--:--");
            this.timerLabel.repaint();
            return;
        }

        long timeLeft = this.world.getTurnManager().getRemainingTime() / 1000;
        if (timeLeft < 5) {
            this.timerLabel.setForeground(Color.RED);
        } else {
            this.timerLabel.setForeground(Color.WHITE);
        }

        String timeString = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60);
        if (world.getTurnManager().getTurn() == null || world.getTurnManager().getTurn().getWormPlayable() == null) {
            timeString = "--:--";
        }

        Color baseColor = switch (playingTeam.getTeamColor()) {
            case RED -> Color.RED;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.GREEN;
            case YELLOW -> Color.YELLOW;
            default -> Color.BLACK;
        };
        Color color = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 100);

        this.timerContainer.setBackground(color);
        this.timerLabel.setText("tour : " + world.getTurnManager().getNbTurn() + "| équipe : " + playingTeam.name + "| tirs restants : "
                + (Turn.FIRE_LIMIT_PER_TURN - playingTeam.getShootCount()) +  "| temps : " + timeString);
        if (playingTeam.getCredits() == -1) {
            this.moneyLabel.setText("∞ $");
        } else {
            this.moneyLabel.setText(playingTeam.getCredits() + " $");
        }
        this.timerLabel.repaint();
        this.moneyLabel.repaint();
    }

    /**
     * cree le label timer.
     * @return le label timer
     */
    private JLabel createTimerLabel() {
        JLabel label = new JLabel("--:--");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        label.setOpaque(false);
        return label;
    }



   ////////////PAUSE/////////////////

   /**
    * crée l'interface du menu pause avec les bouton (quitter reprendre etc) ainsi que les slider de volume.
    */
    private void createPauseMenu() {
        this.pauseMenucontainer = new JPanel(new GridBagLayout()); // un conteneur pour pour centre le vrai pannel
        this.pauseMenucontainer.setOpaque(false);
        this.pauseMenucontainer.setVisible(false);


        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS)); //Y_AXIS bouton les un sous les autre
        menuPanel.setBackground(Color.DARK_GRAY);
        menuPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


        JButton resumeButton = new JButton("Reprendre");
        JButton saveButton = new JButton("Sauvegarder");
        JButton quitButton = new JButton("Quitter");

        JLabel musiqueVolumeLabel = new JLabel("Volume de la musique");
        JLabel sonorEffectVolumeLabel = new JLabel("Volume des effets sonores");
        JSlider musiqueVolumeSlider = Utilitaire.createSlider(0, 100, SoundPlayer.getMusiqueVolume(), 50);
        JSlider soundEffectVolumeSlider = Utilitaire.createSlider(0, 100, SoundPlayer.getSoundEffectVolume(), 50);

        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        musiqueVolumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sonorEffectVolumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        musiqueVolumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        soundEffectVolumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        musiqueVolumeSlider.setSnapToTicks(false);
        musiqueVolumeSlider.setPaintTicks(false);
        soundEffectVolumeSlider.setSnapToTicks(false);
        soundEffectVolumeSlider.setPaintTicks(false);


        resumeButton.addActionListener(e -> pauseMenu(false));
        saveButton.addActionListener(e -> eventBus.postQueued(new SaveEvent()));
        quitButton.addActionListener(e -> {
            eventBus.postQueued(new SaveEvent());
            eventBus.postQueued(new ScreenHomeEvent());
            this.world.stop();
            SoundPlayer.stopMusic();
         });
        musiqueVolumeSlider.addChangeListener(e ->  SoundPlayer.setMusiqueVolume(musiqueVolumeSlider.getValue()));
        soundEffectVolumeSlider.addChangeListener(e -> SoundPlayer.setSoundEffectVolume(soundEffectVolumeSlider.getValue()));

        menuPanel.add(resumeButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(saveButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(quitButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(musiqueVolumeLabel);
        menuPanel.add(musiqueVolumeSlider);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(sonorEffectVolumeLabel);
        menuPanel.add(soundEffectVolumeSlider);


        this.pauseMenucontainer.add(menuPanel);
        this.centerContainer.add(this.pauseMenucontainer, "PAUSE");
    }

    /*
     * affiche/masque le menu pose
     * envoie l'event {@link GamePauseEvent} pour arrete/reprendre le jeu
     */
    private void pauseMenu(boolean show) {
        if (show) {
            cardLayout.show(centerContainer, "PAUSE");
        } else {
            cardLayout.show(centerContainer, "EMPTY");
        }
        this.revalidate();
        this.repaint();
        this.eventBus.postQueued(new GamePauseEvent(show));
        if (!show) {
            this.world.getBus().postQueued(new RequestFocus());
        }
    }

    ///////////////INVENTAIRE///////////////////
    /**
     * initialise le conteneur de l'inventaire contenant les grilles d'armes et d'outils.
     */
    private void createInventoryContainer() {
        this.inventoryContainer = new JPanel();
        this.inventoryContainer.setOpaque(false);
        this.inventoryContainer.setLayout(new GridLayout(1, 2, 20, 0));
        this.inventoryContainer.setBorder(new EmptyBorder(0, 0, 0, 40));

        this.weaponGrid = new JPanel(new GridLayout(0, 4, 10, 10));
        this.toolGrid = new JPanel(new GridLayout(0, 4, 10, 10));

        this.weaponGrid.setOpaque(false);
        this.toolGrid.setOpaque(false);


        JPanel weaponContainer = createSectionPanel("Armes", this.weaponGrid);
        JPanel toolContainer = createSectionPanel("Outils", this.toolGrid);


        weaponContainer.setAlignmentY(Component.TOP_ALIGNMENT);
        toolContainer.setAlignmentY(Component.TOP_ALIGNMENT);

        this.inventoryContainer.add(weaponContainer);
        this.inventoryContainer.add(toolContainer);
        this.centerContainer.add(this.inventoryContainer, "INVENTORY");
    }

    /**
     * crée un panneau de section stylise (avec bordure et titre) pour l'inventaire.
     * @param title le titre de la section
     * @param grid le JPanel que contiendra le contner
     * @return le contner
     */
    private JPanel createSectionPanel(String title, JPanel grid) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(0, 0, 0, 200));

        TitledBorder border = BorderFactory.createTitledBorder(new EmptyBorder(10, 10, 10, 10), title);
        border.setTitleColor(Color.WHITE);
        container.setBorder(border);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(grid, BorderLayout.NORTH);

        container.add(wrapper, BorderLayout.CENTER);
        return container;
    }

    /**
     * ouvre ou ferme l'inventaire.
     */
    private void openCloseInventory() {
        boolean isInventoryOpen = this.inventoryContainer.isVisible(); //demande au cardLayout si la fenetre est visible

        if (isInventoryOpen) {
            cardLayout.show(centerContainer, "EMPTY");
            this.world.getBus().postQueued(new RequestFocus());

        } else {
            createInventoryGrid();
            cardLayout.show(centerContainer, "INVENTORY");
        }

        this.revalidate();
        this.repaint();
    }

    /**
     * remplit la grille de l'inventaire avec les armes et outils disponible pour l'equipe qui joue.
     * crée les boutons pour chaque item et leur associe les evenement de selection
     */
    private void createInventoryGrid() {
        this.weaponGrid.removeAll();
        Inventory currentInventory = world.getTurnManager().getTurn().getWormPlayable().getTeam().getInventory();

        for (WeaponId id : WeaponId.values()) {
            if (id == WeaponId.NONE) {
                continue;
            }
            boolean isUnlocked = currentInventory.isUnlocked(id);
            int nbAmmo = currentInventory.weapons.getAmmo(id);

            JButton itemButton = Utilitaire.createWeaponButton(id, nbAmmo, isUnlocked, currentButtonSize);
            itemButton.setToolTipText(id.getInfo());

            if (isUnlocked && nbAmmo != 0) {
                itemButton.addActionListener(e -> {
                    this.world.getBus().postQueued(new WeaponSelectedEvent(id));
                    openCloseInventory(); // on ferme l'inventaire si on choisie une arme
                });
            } else if (isUnlocked) {
                itemButton.setEnabled(false);
            }
            this.weaponGrid.add(itemButton);
        }

        this.toolGrid.removeAll();
        for (ToolId id : ToolId.values()) {
            if (id == ToolId.NONE) {
                continue;
            }

            int nbAmmo = currentInventory.tools.getAmmo(id);
            boolean isAvailable = nbAmmo != 0;

            JButton itemButton = Utilitaire.createToolButton(id, nbAmmo, true, currentButtonSize);
            itemButton.setToolTipText(id.getInfo());

            if (isAvailable) {
                itemButton.addActionListener(e -> {
                    this.world.getBus().postQueued(new ToolSelectedEvent(id));
                    openCloseInventory();
                });
            } else {
                itemButton.setEnabled(false);
            }
            this.toolGrid.add(itemButton);
        }
        this.weaponGrid.revalidate();
        this.weaponGrid.repaint();
        this.toolGrid.revalidate();
        this.toolGrid.repaint();
    }


    ///////////shop/////////////

    private void createShopContainer() {
        this.shopContainer = new JPanel();
        this.shopContainer.setOpaque(false);
        this.shopContainer.setLayout(new BoxLayout(this.shopContainer, BoxLayout.X_AXIS));

        this.shopWeaponGrid = new JPanel(new GridLayout(0, 4, 10, 10));
        this.shopAmmoGrid = new JPanel(new GridLayout(0, 4, 10, 10));
        this.shopToolGrid = new JPanel(new GridLayout(0, 4, 10, 10));

        this.shopWeaponGrid.setOpaque(false);
        this.shopAmmoGrid.setOpaque(false);
        this.shopToolGrid.setOpaque(false);

        JPanel weaponContainer = createSectionPanel("Armes", this.shopWeaponGrid);
        JPanel ammoContainer = createSectionPanel("Munitions", this.shopAmmoGrid);
        JPanel toolContainer = createSectionPanel("Outils", this.shopToolGrid);

        weaponContainer.setAlignmentY(Component.TOP_ALIGNMENT);
        ammoContainer.setAlignmentY(Component.TOP_ALIGNMENT);
        toolContainer.setAlignmentY(Component.TOP_ALIGNMENT);

        this.shopContainer.add(weaponContainer);
        this.shopContainer.add(Box.createHorizontalStrut(10));
        this.shopContainer.add(ammoContainer);
        this.shopContainer.add(Box.createHorizontalStrut(10));
        this.shopContainer.add(toolContainer);
        this.centerContainer.add(this.shopContainer, "SHOP");
    }

    private void openCloseShop() {
        boolean isOpen = this.shopContainer.isVisible();
        if (isOpen) {
            cardLayout.show(centerContainer, "EMPTY");
            this.world.getBus().postQueued(new RequestFocus());
        } else {
            refreshShopGrid();
            cardLayout.show(centerContainer, "SHOP");
        }
        this.revalidate();
        this.repaint();
    }



    private void refreshShopGrid() {
        this.shopWeaponGrid.removeAll();
        this.shopAmmoGrid.removeAll();
        this.shopToolGrid.removeAll();

        Inventory inventory = world.getPlayingTeam().getInventory();
        int currentMoney = world.getPlayingTeam().getCredits();

        for (ShopElement element : shopManager.getShopElements()) {

            if (element.id instanceof WeaponId weaponId) {
                if (!inventory.isUnlocked(weaponId)) {
                    boolean canBuy = currentMoney >= element.unlockPrice || currentMoney == -1;
                    String text = element.unlockPrice + "$";
                    BufferedImage img = TextureManager.WEAPONS.get(weaponId.toString());

                    JButton buttonShop = Utilitaire.createShopButton(img, text, canBuy);

                    buttonShop.setToolTipText(weaponId.getInfo());

                    buttonShop.addActionListener(e -> {
                        if (canBuy) {
                        shopManager.buyWeapon(weaponId, world.getPlayingTeam());
                        eventBus.postQueued(new GameStateUpdatedEvent());
                    }
                    });

                    this.shopWeaponGrid.add(buttonShop);
                } else {
                    String text = element.ammoPrice + "$ (+" + element.ammoPackSize + ")";
                    BufferedImage imgAmmo = TextureManager.WEAPONS.get("Ammo" + weaponId);
                    boolean canBuy = currentMoney >= element.ammoPrice || currentMoney == -1;

                    JButton buttonShop = Utilitaire.createShopButton(imgAmmo, text, canBuy);

                    buttonShop.setToolTipText(weaponId.getInfo());

                    buttonShop.addActionListener(e -> {
                        if (canBuy) {
                        shopManager.buyAmmo(weaponId, world.getPlayingTeam());
                        eventBus.postQueued(new GameStateUpdatedEvent());
                        }
                    });

                    this.shopAmmoGrid.add(buttonShop);
                }
            } else if (element.id instanceof ToolId tid) {
                String text = element.ammoPrice + "$ (+" + element.ammoPackSize + ")";
                BufferedImage img = TextureManager.TOOLS.get(tid.toString());
                boolean canBuy = currentMoney >= element.ammoPrice || currentMoney == -1;

                JButton btn = Utilitaire.createShopButton(img, text, canBuy);

                btn.setToolTipText(tid.getInfo());

                btn.addActionListener(e -> {
                    shopManager.buyAmmo(tid, world.getPlayingTeam());
                    eventBus.postQueued(new GameStateUpdatedEvent());
                });

                this.shopToolGrid.add(btn);
            }
        }
        this.shopWeaponGrid.revalidate();
        this.shopWeaponGrid.repaint();
        this.shopAmmoGrid.revalidate();
        this.shopAmmoGrid.repaint();
        this.shopToolGrid.revalidate();
        this.shopToolGrid.repaint();
    }

/////////////resize bouton et timer/////////////
    private void resizeUI() {
        int height = getHeight();
        int width = getWidth();
        if (height == 0 || width == 0) return;

        int size = (int) (height * 0.12); // 12% de la hauteur
        size = Math.max(40, Math.min(128, size));

        updateButtonIcon(pauseButton, "Pause", size);
        updateButtonIcon(inventoryButton, "InventoryButton", size);
        updateButtonIcon(skipTurnButton, "SkipTurn", size);
        updateButtonIcon(shopButton, "ShopButton", size);
        updateButtonIcon(changeWormsButton, "ChangeWorms", size); //TODO : Change

        if (timerLabel != null) {
            int fontSize = Math.max(12, size / 4);
            timerLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
        }

        if (this.weaponGrid != null && this.toolGrid != null) {
            int panelWidth = (getWidth() / 2) - 60; // 60 pixel pour al marge
            this.currentButtonSize = panelWidth / 4;

            if (this.currentButtonSize < 1) this.currentButtonSize = 1;

            if (inventoryContainer.isVisible()) {
                createInventoryGrid();
            }
        }
    }

    private void updateButtonIcon(JButton btn, String name, int size) {
        if (btn == null) return;

        BufferedImage img = TextureManager.UI.get(name);
        if (img != null) {
            Image scaled = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaled));
            btn.setPreferredSize(new Dimension(size, size));
        }

        BufferedImage imgHover = TextureManager.UI.get(name + "Hover");
        if (imgHover != null) {
            Image scaledHover = imgHover.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            btn.setRolloverIcon(new ImageIcon(scaledHover));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (world == null) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Chargement", getWidth() / 2, getHeight() / 2);
            return;
        }
        Popup.draw((Graphics2D) g);
    }
}