package com.example.pethouseowner.Common;

import com.example.pethouseowner.Model.User;

public class Common {
    public static User currentUser;

    public static final String UPDATE="Mettre à jour";

    public static final String DELETE="Supprimer";
    public static final   int PICK_IMAGE_REQUEST=71;

    public static String convertCodeToStatus(String code){
        if(code.equals("0"))
            return "Commande acceptée";
        else if(code.equals("1"))
            return "Votre commande est en route";
        else if(code.equals("2"))
            return "Votre animal est disponible,récupérez le auprès du cabinet!";
        else
            return"Votre commande a été livrée";
    }

}
