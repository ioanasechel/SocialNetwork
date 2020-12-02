package socialnetwork.domain;

import java.time.LocalDateTime;


public class Friendship extends Entity<Tuple<Long,Long>> {

    LocalDateTime date;

    /**
     * constructor
     * @param data the date the friendship was created
     */
    public Friendship(LocalDateTime data){
        date = data;
    }

    /**
     * return the date when the friendship was created
     * @return LocalDateTime
     */
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return getId().getLeft() + " and " + getId().getRight()+ " "+
                "became friends on the date " + date;
    }

}
