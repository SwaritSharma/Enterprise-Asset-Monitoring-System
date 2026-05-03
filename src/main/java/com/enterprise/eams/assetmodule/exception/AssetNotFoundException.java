package com.enterprise.eams.assetmodule.exception;

public class AssetNotFoundException extends RuntimeException{
    public AssetNotFoundException(String message) {
        super(message);
    }
}
