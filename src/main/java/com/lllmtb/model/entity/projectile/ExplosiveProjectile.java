package com.lllmtb.model.entity.projectile;

public interface ExplosiveProjectile {
    /** Methods related to explosive projectiles.
     * @return explosion radius
     */
    int getExplosionRadius();

    /** Methods related to explosive projectiles.
     * @return souffle radius
     */
    int getSuffleRadius();
    /** Methods related to explosive projectiles.
     * @return explosion damage
     */
    int getExplosionDamage();
    /** Methods related to explosive projectiles.
     * @return explosion force
     */
    double getExplosionForce();
    /** Methods related to explosive projectiles.
     * @return explosion texture key
     */
    String getExplosionTexture();
}
