package de.schweininchen.shintaikan.shintaikan.jetpack

enum class NavigationDrawerRoutes(val id: String) {
    HOME("home") { override fun toString(): String = "Shintaikan" },
    TRPLAN("trplan") { override fun toString(): String = "Trainingsplan" },
    PRUEFUNGEN("pruefungen") { override fun toString(): String = "Gürtelprüfungen" },
    FERIEN("ferientraining") { override fun toString(): String = "Ferientraining" },
    NACHSOFE("nachsofe") { override fun toString(): String = "Nach den Sommerferien" },
    CLUBWEG("clubweg") { override fun toString(): String = "Der Club / Wegbeschreibung" },
    ANFAENGER("anfaenger") { override fun toString(): String = "Anfänger / Interressenten" },
    VORFUEHRUNGEN("vorfuehrungen") { override fun toString(): String = "Vorführungen" },
    LEHRGAENGE("turniere") { override fun toString(): String = "Lehrgänge & Turniere" },
    COLORS("colors") { override fun toString(): String = BuildConfig.BUILD_TYPE },
    NONE("")
}