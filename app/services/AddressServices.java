package services;

import exceptions.CreationException;
import models.Address;
import models.MobileUser;

import java.util.Arrays;

public class AddressServices {

    public static void create(Address address, MobileUser user) throws CreationException{
        if(address.getId() == null){
            Address dbAddress = Address.findByProperties(Arrays.asList("street", "number", "additionalInformation", "user.id"),
                                                        Arrays.asList(address.getStreet(), address.getNumber(), address.getAdditionalInformation(), user.getId()));
            if(dbAddress == null) {
                user.getAddresses().add(address);
                user.update();
            }else {
                address.setId(dbAddress.getId());
            }
        }else{
            Address idAddress = Address.findByProperties(Arrays.asList("id", "user.id"), Arrays.asList(address.getId(), user.getId()));
            if(idAddress == null){
                throw new CreationException("Id de direccion incorrecta");
            }
        }
    }
}
