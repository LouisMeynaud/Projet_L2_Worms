package com.lllmtb.model.shop;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.item.inventory.Inventory;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;

class ShopManagerTest {

    private ShopManager shopManager;
    private Team mockTeam;
    private Inventory mockInventory;

    @BeforeEach
    void setUp() {
        shopManager = new ShopManager();
        
        mockTeam = Mockito.mock(Team.class);
        mockInventory = Mockito.mock(Inventory.class);
        when(mockTeam.getInventory()).thenReturn(mockInventory);
    }

    @Test
    void initShopTest() {
        assertFalse(shopManager.getShopElements().isEmpty());
    }

    @Test
    void buyItem_unlocksWeapon_whenEnoughCredits() {
        when(mockTeam.getCredits()).thenReturn(100);

        shopManager.buyItem(WeaponId.GUN, mockTeam);

        verify(mockTeam).removeCredits(50); 
        verify(mockTeam).unlockWeapon(WeaponId.GUN);
    }

    @Test
    void buyItem_doesNothing_whenNotEnoughCredits() {
        when(mockTeam.getCredits()).thenReturn(10); 

        shopManager.buyItem(WeaponId.GUN, mockTeam);

        verify(mockTeam, never()).removeCredits(50); // never pour verifier que removeCredits n est pas appeler
        verify(mockTeam, never()).unlockWeapon(WeaponId.GUN);
    }

    @Test
    void buyAmmo_addsAmmo_whenUnlockedAndEnoughCredits() {
        when(mockInventory.isUnlocked(WeaponId.GUN)).thenReturn(true);
        when(mockTeam.getCredits()).thenReturn(100);

        shopManager.buyAmmo(WeaponId.GUN, mockTeam);

        verify(mockTeam).removeCredits(20); 
        verify(mockTeam).addAmmo(WeaponId.GUN, 5); 
    }

    @Test
    void buyAmmo_doesNothing_whenWeaponLocked() {
        when(mockInventory.isUnlocked(WeaponId.GUN)).thenReturn(false); 
        when(mockTeam.getCredits()).thenReturn(100);

        shopManager.buyAmmo(WeaponId.GUN, mockTeam);
        verify(mockTeam, never()).removeCredits(50);
    }

    @Test
    void buyAmmo_worksForTools() {
        when(mockTeam.getCredits()).thenReturn(100);

        shopManager.buyAmmo(ToolId.PARACHUTE, mockTeam);

        verify(mockTeam).removeCredits(20); 
        verify(mockTeam).addAmmo(ToolId.PARACHUTE, 1);
    }
}