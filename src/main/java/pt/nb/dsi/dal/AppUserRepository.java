package pt.nb.dsi.dal;


import java.util.List;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppUserRepository implements PanacheRepositoryBase<AppUser, UUID> {

    public AppUser findByName(String name){
        return find("username", name).firstResult();
    }
    
    public List<AppUser> listAllRoleUser() {
        return list("role", "USER");
    }
    
}
