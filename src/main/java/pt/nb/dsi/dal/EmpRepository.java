package pt.nb.dsi.dal;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmpRepository implements PanacheRepository<Emp> {
    // Add custom query methods here if needed
}
