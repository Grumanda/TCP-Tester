package de.gozilalp.configSetup;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatLightOwlIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterIJTheme;

public enum DefinedLAFs {

    FLAT_LIGHT_LAF("FLAT_LIGHT_LAF", false),
    FLAT_ARC_ORANGE_IJTHEME("FLAT_ARC_ORANGE_IJTHEME", false),
    FLAT_ATOM_ONE_LIGHT_IJTHEME("FLAT_ATOM_ONE_LIGHT_IJTHEME", false),
    FLAT_MATERIAL_LIGHTER_IJTHEME("FLAT_MATERIAL_LIGHTER_IJTHEME", false),
    FLAT_LIGHT_OWL_IJTHEME("FLAT_LIGHT_OWL_IJTHEME", false),
    FLAT_DARK_LAF("FLAT_DARK_LAF", true),
    FLAT_MONOKAI_PRO_IJTHEME("FLAT_MONOKAI_PRO_IJTHEME", true),
    FLAT_MATERIAL_DESIGN_DARK_IJTHEME("FLAT_MATERIAL_DESIGN_DARK_IJTHEME", true),
    FLAT_ARC_DARK_IJTHEME("FLAT_ARC_DARK_IJTHEME", true),
    FLAT_ONE_DARK_IJTHEME("FLAT_ONE_DARK_IJTHEME", true);




    private String configValue;
    private boolean isDarkTheme;

    DefinedLAFs(String configValue, boolean isDarkTheme) {
        this.configValue = configValue;
        this.isDarkTheme = isDarkTheme;
    }

    public String getConfigValue() {
        return configValue;
    }

    public boolean isDarkTheme() {
        return isDarkTheme;
    }
}
