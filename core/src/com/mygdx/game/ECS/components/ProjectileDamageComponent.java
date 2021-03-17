package com.mygdx.game.ECS.components;

import com.badlogic.ashley.core.Component;

public class ProjectileDamageComponent implements Component {
    int damage;
    int blast_radius;

     public ProjectileDamageComponent(int damage, int blast_radius){
         this.damage = damage;
         this.blast_radius = blast_radius;
     }
}
