package socialnetwork.domain;

import java.io.Serializable;

public class Entity<ID> implements Serializable {

    private static final long serialVersionUID = 7331115341259248461L;
    private ID id;

    /**
     * get id
     * @return id ID
     */
    public ID getId() {
        return id;
    }

    /**
     * set id
     * @param id ID
     */
    public void setId(ID id) {
        this.id = id;
    }
}