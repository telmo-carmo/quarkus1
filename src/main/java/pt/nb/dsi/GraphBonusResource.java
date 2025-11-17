package pt.nb.dsi;

/*
 * use it:
 * 
 * curl http://localhost:8080/graphql/schema.graphql
 * 
 * http://localhost:8080/q/graphql-ui/ 
 * 
 */
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;

import jakarta.inject.Inject;
import pt.nb.dsi.dal.Bonus;
import pt.nb.dsi.dal.IBonusRepository;

@GraphQLApi
public class GraphBonusResource {

    @Inject
    IBonusRepository bonusRepository;

    /*
    query allBonus {
        allBonus {
            ename
            job

        }
    }

    query getBonus {
        bonus(bonusId: "JONES") {
            ename
            job
            sal
        }
    }
     */
    @Query("allBonus") 
    @Description("Get all Bonus from a galaxy far far away") 
    public List<Bonus> getAllBonus() {
        return bonusRepository.findAll();
    }

    @Query
    @Description("Get one Bonus by name")
    public Bonus getBonus(@Name("bonusId") String id) {
        return bonusRepository.findOne(id);
    }
}
