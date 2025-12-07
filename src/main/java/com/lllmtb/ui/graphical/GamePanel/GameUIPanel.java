package com.lllmtb.ui.graphical.GamePanel;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.*;
import com.lllmtb.events.ui.ShootMode;
import com.lllmtb.events.ui.graphical.RequestFocus;
import com.lllmtb.model.item.inventory.Inventory;
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
    private JPanel inventoryGrid;


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




        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setOpaque(false);

        JPanel lowerBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lowerBar.setOpaque(false);




        this.add(topBar, BorderLayout.NORTH);
        this.add(lowerBar, BorderLayout.SOUTH);

        JButton pauseButton = Utilitaire.createImageButton(TextureManager.UI, "Pause", 64, 64, 48, 48);
        JButton inventoryButton = Utilitaire.createImageButton(TextureManager.UI, "InventoryButton", 64, 64, 48, 48);
        JButton shootModeButton = Utilitaire.createImageButton(TextureManager.UI, "ShootMode", 64, 64, 48, 48);
        pauseButton.addActionListener(e -> pauseMenu(true));
        shootModeButton.addActionListener(e -> this.eventBus.postQueued(new ShootMode()));
        inventoryButton.addActionListener(e -> openCloseInventory());
        topBar.add(pauseButton);
        lowerBar.add(inventoryButton);
        lowerBar.add(shootModeButton);

    }


   ////////////PAUSE/////////////////

    private void createPauseMenu() {
        this.pauseMenucontainer = new JPanel(new GridBagLayout()); // un conteneur pour pour centre le vrai pannel
        this.pauseMenucontainer.setOpaque(false);


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
    if (!show) {
        this.eventBus.postQueued(new RequestFocus());
    }
    }

    ///////////////INVENTAIRE/////////////
    private void createInventoryContainer() {
        this.inventoryContainer = new JPanel(new GridBagLayout());
        this.inventoryContainer.setOpaque(false);


        this.inventoryGrid = new JPanel();

        this.inventoryGrid.setBackground(new Color(0, 0, 0, 200));
        this.inventoryGrid.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.inventoryGrid.setLayout(new GridLayout(0, 6, 10, 10));
        this.inventoryContainer.add(this.inventoryGrid);
        this.centerContainer.add(this.inventoryContainer, "INVENTORY");
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

            createInventoryGrid();
            cardLayout.show(centerContainer, "INVENTORY");
        }

        this.revalidate();
        this.repaint();
    }

    private void createInventoryGrid() {
        this.inventoryGrid.removeAll(); //le vide
        Inventory currentInventory = world.getTurnManager().getTurn().getWormPlayable().getTeam().getInventory();

        for (WeaponId id : WeaponId.values()) {
            if (id == WeaponId.NONE) {
                continue;
            }
            boolean isUnlocked = currentInventory.isUnlocked(id);
            int nbAmmo = currentInventory.weapons.getAmmo(id);

            JButton itemButton = Utilitaire.createWeaponButton(id, "Ammo" + id, nbAmmo, isUnlocked);

            if (isUnlocked && nbAmmo > 0) {
                itemButton.addActionListener(e -> {
                    eventBus.postQueued(new WeaponSelectedEvent(id));
                    openCloseInventory(); // on ferme l'inventaire si on choisie une arme
                });
            } else if (isUnlocked && nbAmmo == 0) {
                itemButton.setEnabled(false);
            }

            this.inventoryGrid.add(itemButton);

        }
    }

}
