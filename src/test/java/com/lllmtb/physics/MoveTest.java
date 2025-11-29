package com.lllmtb.physics;

import com.lllmtb.engine.commands.AddPhysicsModifierCommand;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.ui.graphical.GamePanel.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {
    @BeforeEach
    void setUp() {
        // On remet le saut à 0 avant chaque test
        Move.jump = 0;
    }

    @Test
    void testJumpToMax() {
        Move.JumpToMax();
        assertEquals(2, Move.jump);
    }
    @Test
    void testMoveLeft() throws NoSuchFieldException, IllegalAccessException {
        Worm wormMock = Mockito.mock(Worm.class);
        GameCommand gameCommand =   Move.move(wormMock, Direction.Left);
        Field modifierField = AddPhysicsModifierCommand.class.getDeclaredField("modifier");
        modifierField.setAccessible(true);
        PhysicsModifier m = (PhysicsModifier) modifierField.get(gameCommand);
        Transform t =m.transform;
        Vector v = new Vector(0,0);
        Vector vector = t.apply(v);


        assertNotNull(gameCommand, "La commande ne doit pas être null");
        assertInstanceOf(AddPhysicsModifierCommand.class, gameCommand, "La commande doit être de type AddPhysicsModifierCommand");
        assertTrue(m.oneShot);
        assertEquals(-5.0, vector.getX());
        assertEquals(v.getY(), vector.getY());
    }

    @Test
    void testMoveLRight() throws NoSuchFieldException, IllegalAccessException {
        Worm wormMock = Mockito.mock(Worm.class);
        GameCommand gameCommand =   Move.move(wormMock, Direction.Right);
        Field modifierField = AddPhysicsModifierCommand.class.getDeclaredField("modifier");
        modifierField.setAccessible(true);
        PhysicsModifier m = (PhysicsModifier) modifierField.get(gameCommand);
        Transform t =m.transform;
        Vector v = new Vector(0,0);
        Vector vector = t.apply(v);


        assertNotNull(gameCommand, "La commande ne doit pas être null");
        assertInstanceOf(AddPhysicsModifierCommand.class, gameCommand, "La commande doit être de type AddPhysicsModifierCommand");
        assertTrue(m.oneShot);
        assertEquals(5.0, vector.getX());
        assertEquals(v.getY(), vector.getY());
    }

    @Test
    void testMoveUpWithoutJump() {
        Worm wormMock = Mockito.mock(Worm.class);
        GameCommand gameCommand =   Move.move(wormMock, Direction.Up);
        assertNull(gameCommand);
    }
    @Test
    void testMoveUpWithJump() throws NoSuchFieldException, IllegalAccessException {
        Move.JumpToMax();
        Worm wormMock = Mockito.mock(Worm.class);
        GameCommand gameCommand =   Move.move(wormMock, Direction.Up);
        Field modifierField = AddPhysicsModifierCommand.class.getDeclaredField("modifier");
        modifierField.setAccessible(true);
        PhysicsModifier m = (PhysicsModifier) modifierField.get(gameCommand);
        Transform t =m.transform;
        Vector v = new Vector(0,0);
        Vector vector = t.apply(v);


        assertNotNull(gameCommand, "La commande ne doit pas être null");
        assertInstanceOf(AddPhysicsModifierCommand.class, gameCommand, "La commande doit être de type AddPhysicsModifierCommand");
        assertTrue(m.oneShot);
        assertEquals(v.getX(), vector.getX());
        assertEquals(20, vector.getY());
    }
    @Test
    void testMoveDown() {
        Worm wormMock = Mockito.mock(Worm.class);
        GameCommand gameCommand =   Move.move(wormMock, Direction.Down);
        assertNull(gameCommand);
    }

}
