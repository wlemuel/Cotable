package tk.wlemuel.cotable.model;

/**
 * Entity
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc Entity
 * @created 2015/05/09
 * @updated 2015/05/09
 */
public abstract class Entity extends Base {

    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected String cacheKey;

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
