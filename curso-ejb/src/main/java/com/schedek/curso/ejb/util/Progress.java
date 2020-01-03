/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.util;

/**
 *
 * @author Adam
 */
public class Progress {

    public int state;
    public int total;

    public Progress(int state, int total) {
        this.state = state;
        this.total = total;
    }

    public int getProgress() {
        if (total == 0) {
            return 0;
        }
        return (100 * state) / total;
    }
}
