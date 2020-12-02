package socialnetwork.repository.memory;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private Validator<E> validator;
    public Map<ID,E> entities;

    /**
     * constructor
     * @param validator the validator
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public E findOne(ID id) {
        //E user = entities.get(id);
        if(id == null)
            throw new ValidationException("id " + id + " nu exista");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            throw new ValidationException("ID existent!");
        }
        else {
            entities.put(entity.getId(), entity);

        }
        return null;
    }

    @Override
    public E delete(ID id)  {
        E elem = findOne(id);
        entities.keySet().removeIf(x->x.equals(id));
        return elem;

    }

    @Override
    public E update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;
    }

    public int count(){
        return entities.size();
    }

    public boolean exists(ID id){
        if (findOne(id)==null)
            return false;
        else
            return true;
    }
}
