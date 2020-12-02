package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;

import java.util.Arrays;
import java.util.List;


///Aceasta clasa implementeaza sablonul de proiectare Template Method; puteti inlucui
// solutia propusa cu un Factori (vezi mai jos)
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;

    /**
     * constructor
     * @param fileName the file name
     * @param validator the validator
     */
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
    }

    /**
     * upload data from file
     */
    protected void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){
                List<String> attr=Arrays.asList(linie.split(";"));
                E e=extractEntity(attr);
                super.save(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public abstract E extractEntity(List<String> attributes);

    protected abstract String createEntityAsString(E entity);

    /**
     * save the entity
     * @param entity the entitu
     * @return the entity
     */
    @Override
    public E save(E entity){
        E e=super.save(entity);
        if (e==null)
        {
            writeToFile(entity);
        }
        return e;

    }

    /**
     * function that empties a file
     */
    private void emptyFile(){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName, false))) {
            bW.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function that writes each entity to the file
     */
    protected void writeAll(){
        emptyFile();
        entities.values().forEach(this::writeToFile);
    }

    /**
     * function that deletes an entity from the file
     * @param id the entity id
     * @return the entity
     */
    @Override
    public E delete(ID id) {
        E rez = super.delete(id);
        writeAll();
        return rez;
    }

    /**
     * function that writes an entity to the file
     */
       protected void writeToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }







}

