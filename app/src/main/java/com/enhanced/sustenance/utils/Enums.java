package com.enhanced.sustenance.utils;

public class Enums {
    public enum CATEGORIES {
        FRUIT("Fruit"),
        VEG("Veg"),
        MEAT("Meat"),
        DAIRY("Dairy");

        private final String label;

        CATEGORIES(String label) {
            this.label = label;
        }

        public String getValue() {
            return label;
        }

        public static CATEGORIES getEnum(String label) {
            for(CATEGORIES l: CATEGORIES.values()) {
                if(l.label.equals(label)) {
                    return l;
                }
            }
            return null;
        }

        @Override public String toString(){
            return label;
        }
    }

    public enum PACKAGING {
        PLASTIC("Plastic"),
        PAPER("Paper"),
        CARDBOARD("Cardboard");

        private final String label;

        PACKAGING(String label) {
            this.label = label;
        }

        public String getValue() {
            return label;
        }

        public static PACKAGING getEnum(String label) {
            for(PACKAGING l: PACKAGING.values()) {
                if(l.label.equals(label)) {
                    return l;
                }
            }
            return null;
        }

        @Override public String toString(){
            return label;
        }
    }

        public enum UNITS {
            GRAMS("g"),
            OUNCE("oz"),
            KILOGRAMS("kg"),
            POUND("lb"),

            MILLS("ml"),
            LITRES("lt"),
            GALLON("gal"),
            FLOUNCE("fl. oz"),
            QUART("qt"),
            PINT("pt"),

            QTRCUP("1/4 cup"),
            THRDCUP("1/3 cup"),
            HALFCUP("1/2 cup"),
            CUP("1 cup"),

            QTRTSP("1/4 tsp"),
            THRDTSP("1/3 tsp"),
            HALFTSP("1/2 tsp"),
            TSP("1 tsp"),

            QTRTBSP("1/4 tbsp"),
            THRDTBSP("1/3 tbsp"),
            HALFTBSP("1/2 tbsp"),
            TBSP("1 tbsp"),

            SPRIG("sprig"),
            BUNCH("bunch"),
            LEAF("leaf"),
            LOAF("loaf"),
            ITEM("item");

            private final String label;

            UNITS(String label) {
                this.label = label;
            }

            public String getValue() {
                return label;
            }

            public static UNITS getEnum(String label) {
                for(UNITS l: UNITS.values()) {
                    if(l.label.equals(label)) {
                        return l;
                    }
                }
                return null;
            }

            @Override public String toString(){
                return label;
            }
    }


}
