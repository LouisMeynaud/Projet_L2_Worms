package com.lllmtb.physics;

public interface IPhysicsEngine {
    void add(PhysicsBody body); //Ajoute un PhysicsBody à la collection
    
    void delete(PhysicsBody body); //Supprime un PhysicsBody de la collection
    
    void clear(); //Supprime tous les PhysicsBody de la collection
    
    void next(); //Actualise la collection de PhysicsBody
}
