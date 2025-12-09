package com.lllmtb.engine.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.lllmtb.model.item.weapon.BazookaWeapon;
import com.lllmtb.model.item.weapon.GunWeapon;
import com.lllmtb.model.item.weapon.Weapon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Load {
    public static GameSave loadGameSave(String name) throws FileNotFoundException {
        String stringpath = "src/test/resources/SaveTest/" + name + "/test.json";
        RuntimeTypeAdapterFactory<Weapon> weaponAdapter = RuntimeTypeAdapterFactory.of(Weapon.class, "type")
                .registerSubtype(BazookaWeapon.class, "bazookaweapon") // Si le JSON contient "type": "bazooka", on crée un Bazooka
                .registerSubtype(GunWeapon.class, "gunweapon");

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapterFactory(weaponAdapter)
                .create();

        try (FileReader reader = new FileReader(stringpath)) {
            GameSave save = gson.fromJson(reader, GameSave.class);
            System.out.println(save);
            return save;
    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
