package com.lllmtb.ui.graphical.GamePanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.GamePauseEvent;
import com.lllmtb.events.game.SaveEvent;
import com.lllmtb.events.game.ToolSelectedEvent;
import com.lllmtb.events.game.WeaponSelectedEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.events.turn.TurnSkipEvent;
import com.lllmtb.events.ui.ShootMode;
import com.lllmtb.events.ui.graphical.RequestFocus;
import com.lllmtb.model.item.inventory.Inventory;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire;

public class GameUIPanel extends JPanel {

    private EventBus eventBus;

    private JPanel pauseMenucontainer;
    private JPanel inventoryContainer;
    private GameWorld world;
    private final CardLayout cardLayout;
    private final JPanel centerContainer;
    private JPanel weaponGrid;
    private JPanel toolGrid;
    private JLabel timerLabel;
    private Timer uiUpdateTimer;

    public GameUIPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        setOpaque(false);
        setLayout(new BorderLayout());


        //create contner
        this.cardLayout = new CardLayout();
        this.centerContainer = new JPanel(cardLayout);
        this.centerContainer.setOpaque(false);

        //empty panel for view the game if there are not pannel selected
        JPanel emptyCard = new JPanel();
        emptyCard.setOpaque(false);
        this.centerContainer.add(emptyCard, "EMPTY");
        this.add(this.centerContainer, BorderLayout.CENTER);

        createPauseMenu();
        createInventoryContainer();
        eventBus.subscribe(WorldCreatedEvent.class, event -> {
            this.world = event.getWorld(); });




        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(10, 10, 0, 10));

        JPanel lowerBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lowerBar.setOpaque(false);


        this.add(topBar, BorderLayout.NORTH);
        this.add(lowerBar, BorderLayout.SOUTH);


        JButton pauseButton = Utilitaire.createImageButton(TextureManager.UI, "Pause", 64, 64, 48, 48);
        JButton inventoryButton = Utilitaire.createImageButton(TextureManager.UI, "InventoryButton", 64, 64, 48, 48);
        JButton shootModeButton = Utilitaire.createImageButton(TextureManager.UI, "ShootMode", 64, 64, 48, 48);
        JButton skipTurnButton = Utilitaire.createImageButton(TextureManager.UI, "skipTurn", 64, 64, 48, 48);
        pauseButton.addActionListener(e -> pauseMenu(true));
        shootModeButton.addActionListener(e -> this.eventBus.postQueued(new ShootMode()));
        inventoryButton.addActionListener(e -> openCloseInventory());
        skipTurnButton.addActionListener(e -> this.eventBus.postQueued(new TurnSkipEvent()));


        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(pauseButton);
        buttonsPanel.add(skipTurnButton);

        lowerBar.add(inventoryButton);
        lowerBar.add(shootModeButton);


        this.timerLabel = createTimerLabel();

        JPanel timerContner = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerContner.setOpaque(false);
        timerContner.add(this.timerLabel);


        topBar.add(timerContner, BorderLayout.CENTER);
        topBar.add(buttonsPanel, BorderLayout.WEST);


        startUITimer();

    }

    ////////////Timer/////////////////

    private void startUITimer() {
        this.uiUpdateTimer = new Timer(100, e -> {
            updateTimerDisplay();
        });
        this.uiUpdateTimer.start();
    }

    private void updateTimerDisplay() {
        if (this.world == null || this.world.getPlayingTeam() == null) {
            return;
        }

        String teamName = this.world.getPlayingTeam().name;
        long timeLeft = this.world.getTurnManager().getRemainingTime() / 1000;
        if (timeLeft < 5) {
            this.timerLabel.setForeground(Color.RED);
        } else {
            this.timerLabel.setForeground(Color.WHITE);
        }

        String timeString = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60);
        this.timerLabel.setText(teamName + " : " + timeString);
    }

    private JLabel createTimerLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setOpaque(false);
        return label;
    }



   ////////////PAUSE/////////////////

    private void createPauseMenu() {
        this.pauseMenucontainer = new JPanel(new GridBagLayout()); // un conteneur pour pour centre le vrai pannel
        this.pauseMenucontainer.setOpaque(false);
        this.pauseMenucontainer.setVisible(false);


        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS)); //Y_AXIS bouton les un sous les autre
        menuPanel.setBackground(Color.DARK_GRAY);
        menuPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


        JButton resumeButton = new JButton("Reprendre");
        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quitter");


        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        resumeButton.addActionListener(e -> pauseMenu(false));
        saveButton.addActionListener(e -> eventBus.postQueued(new SaveEvent()));
        quitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(resumeButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(saveButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(quitButton);


        this.pauseMenucontainer.add(menuPanel);


        this.centerContainer.add(this.pauseMenucontainer, "PAUSE");
    }


    private void pauseMenu(boolean show) {

    if (show) {
        cardLayout.show(centerContainer, "PAUSE");
    } else {
        cardLayout.show(centerContainer, "EMPTY");
    }
    this.revalidate();
    this.repaint();
    this.eventBus.postQueued(new GamePauseEvent(show));
    Logger.logger.debug("Game Paused");
    if (!show) {
        this.eventBus.postQueued(new RequestFocus());
        Logger.logger.debug("Game Unpaused");
    }
    }

    ///////////////INVENTAIRE/////////////
    private void createInventoryContainer() {
        this.inventoryContainer = new JPanel();
        this.inventoryContainer.setOpaque(false);
        this.inventoryContainer.setLayout(new BoxLayout(this.inventoryContainer, BoxLayout.X_AXIS));

        this.weaponGrid = new JPanel(new GridLayout(0, 6, 10, 10));
        this.toolGrid = new JPanel(new GridLayout(0, 6, 10, 10));

        this.weaponGrid.setOpaque(false);
        this.toolGrid.setOpaque(false);


        JPanel weaponContainer = createSectionPanel("Weapons", this.weaponGrid);
        JPanel toolContainer = createSectionPanel("Tools", this.toolGrid);


        weaponContainer.setAlignmentY(Component.TOP_ALIGNMENT);
        toolContainer.setAlignmentY(Component.TOP_ALIGNMENT);

        this.inventoryContainer.add(weaponContainer);
        this.inventoryContainer.add(Box.createHorizontalStrut(20));
        this.inventoryContainer.add(toolContainer);
        this.centerContainer.add(this.inventoryContainer, "INVENTORY");
    }


    private JPanel createSectionPanel(String title, JPanel grid) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(0, 0, 0, 200));

        TitledBorder border = BorderFactory.createTitledBorder(new EmptyBorder(10, 10, 10, 10), title);
        border.setTitleColor(Color.WHITE);
        container.setBorder(border);

        container.add(grid, BorderLayout.NORTH);
        return container;
    }

    private void openCloseInventory() {
        boolean isInventoryOpen = this.inventoryContainer.isVisible(); //demande au cardLayout si la fenetre est visible

        if (isInventoryOpen) {
            cardLayout.show(centerContainer, "EMPTY");
            this.eventBus.postQueued(new RequestFocus());

        } else {
            //ces deux ligne sont pour tester que l'affichage s'adapte bien
            this.world.getTurnManager().getTurn().getTeam().getInventory().unlock(WeaponId.GUN);
            this.world.getTurnManager().getTurn().getTeam().getInventory().weapons.addAmmo(WeaponId.GUN, 10);
            this.world.getTurnManager().getTurn().getTeam().getInventory().tools.addAmmo(ToolId.PARACHUTE, 2);

            createInventoryGrid();
            cardLayout.show(centerContainer, "INVENTORY");
        }

        this.revalidate();
        this.repaint();
    }

    private void createInventoryGrid() {
        this.weaponGrid.removeAll(); //le vide
        Inventory currentInventory = world.getTurnManager().getTurn().getWormPlayable().getTeam().getInventory();

        for (WeaponId id : WeaponId.values()) {
            if (id == WeaponId.NONE) {
                continue;
            }
            boolean isUnlocked = currentInventory.isUnlocked(id);
            int nbAmmo = currentInventory.weapons.getAmmo(id);

            JButton itemButton = Utilitaire.createWeaponButton(id, nbAmmo, isUnlocked);

            if (isUnlocked && nbAmmo > 0) {
                itemButton.addActionListener(e -> {
                    eventBus.postQueued(new WeaponSelectedEvent(id));
                    openCloseInventory(); // on ferme l'inventaire si on choisie une arme
                });
            } else if (isUnlocked && nbAmmo == 0) {
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
            boolean isAvailable = nbAmmo > 0;

            JButton itemButton = Utilitaire.createToolButton(id, nbAmmo, true);

            if (isAvailable) {
                itemButton.addActionListener(e -> {
                    eventBus.postQueued(new ToolSelectedEvent(id));
                    openCloseInventory();
                });
            } else {
                itemButton.setEnabled(false);
            }
            this.toolGrid.add(itemButton);
        }
    }


}
