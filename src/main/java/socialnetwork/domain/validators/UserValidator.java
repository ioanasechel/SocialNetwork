package socialnetwork.domain.validators;

import socialnetwork.domain.User;

public class UserValidator implements Validator<User> {

    /**
     * validate first name
     * @param firstName the first name
     * @return true if the first name length is greater than 0; false otherwise
     */
    private boolean validFirstName(String firstName){
        return firstName.length()!=0;
    }

    /**
     * validate last name
     * @param lastName the last name
     * @return true if the last name length is greater than 0; false otherwise
     */
    private boolean validLastName(String lastName){
        return lastName.length()!=0;
    }

    /**
     * validates an id
     * @param id the id
     * @return true if id is greater than 0; false otherwise
     */
    private boolean validId(long id){
        return id>0;
    }

    /**
     * validate a user
     * @param entity the user
     * @throws ValidationException Exception
     */
    @Override
    public void validate(User entity) throws ValidationException {
        String err="";
        if (!validId(entity.getId())){
            err+="Invalid id\n";
        }
        if (!validFirstName(entity.getFirstName())){
            err+="Invalid first name\n";
        }
        if (!validLastName(entity.getLastName())){
            err+="Invalid last name\n";
        }
        if (err.length()!=0){
            throw new ValidationException(err);
        }
    }
}
