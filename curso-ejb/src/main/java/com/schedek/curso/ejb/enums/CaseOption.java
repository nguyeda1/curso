package com.schedek.curso.ejb.enums;

public enum CaseOption {
    YES("Ano"), NO("Ne"), NOT_NECESSARY("Není potřeba");

    String label;

    CaseOption() {
    }

    CaseOption(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
