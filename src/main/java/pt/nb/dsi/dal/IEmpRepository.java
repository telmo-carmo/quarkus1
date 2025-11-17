package pt.nb.dsi.dal;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface IEmpRepository extends PanacheRepository<Emp> {
    // Add custom query methods here if needed
}
