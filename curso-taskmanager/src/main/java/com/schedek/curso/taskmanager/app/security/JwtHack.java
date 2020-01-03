/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Root
 */
public class JwtHack {
		public static  void main(String args[]){
        String token= Jwts.builder()
                .setSubject(Long.toString(13L))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + TimeUnit.DAYS.toMillis(10)))
                .signWith(SignatureAlgorithm.HS512, "CursoTaskManagerSecretKey")
                .compact();
		System.out.println(token);
	}
}
