package socialnetwork.domain.validators;

import socialnetwork.domain.Message;

public class MessageValidator implements Validator<Message>{

    /**
     * validates an id
     * @param id the id
     * @return true if id is greater than 0; false otherwise
     */
    private boolean validId(Long id){
        return id>0;
    }

    /**
     * validates a message
     * @param message the message
     * @return true if message length is greater than 0; false otherwise
     */
    private boolean validMessage(String message){
        return message.length()!=0;
    }

    /**
     * validates a message
     * @param entity the message
     * @throws ValidationException
     */
    @Override
    public void validate(Message entity) throws ValidationException {

        String err="";
        if (!validId(entity.getId())){
            err+="Invalid id\n";
        }
        if (!validMessage(entity.getMessage())){
            err+="Invalid message\n";
        }

        if (err.length()!=0) {
            throw new ValidationException(err);
        }
    }
}
