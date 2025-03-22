package pt.nb.dsi.dal;

import java.util.List;

public interface IBonusRepository {
    public int          count();
    public List<Bonus>  findAll();
    public Bonus        findOne(String ename);
    public Bonus        save(Bonus bn);
    public boolean      update(Bonus b);
    public boolean      delete(String ename);
    
    public List<Bonus>  findRange(int from, int to);
}

