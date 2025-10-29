package com.lllmtb.physics;

/**
 * Permet le stockage d'une collection d'objets de type PhysicsBody (ceux associés aux objets Enity existants au moment présent).
 * Permet la gestion de cette collection et notamment sa mise à jour.
 * Une implémentation de cette interface sera utilisée pour la gestion des déplacements/trajectoires des objets Entity associés aux PhysicsBody de la collection
 * mais également pour le déclenchement des effets procurés par une collision aux objets Entity associés aux PhysicsBody de la collection.
 */
public interface IPhysicsEngine {

    /**
     * Ajoute un PhysicsBody à la collection.
     * @param body le PhysicsBody à ajouter (s'il est null, la collection de PhysicsBody reste inchangée)
     */
    void add(PhysicsBody body);

    /**
     * Supprime un PhysicsBody de la collection.
     * @param body le PhysicsBody à supprimer (s'il n'est pas présent, la collection de PhysicsBody reste inchangée)
     */
    void remove(PhysicsBody body);

    /**
     * Supprime tous les PhysicsBody de la collecion.
     */
    void clear();

    /**
     * Met à jour l’état de tous les PhysicsBody de la collection en appliquant les lois physiques (mouvement, forces, collisions, etc.)
     * et en déclenchants des effets (explosion, etc.).
     * Cette méthode est appelée à chaque tick du jeu.
     */
    void next();
}
