package com.example.shoprunner_be.exceptions;

public class Dataintegrityviolationexception extends RuntimeException{
    public Dataintegrityviolationexception(String message){
        super(message);
    }
}
