package aa;

/**
 * An interface containing exactly one abstract method used to implement the
 * core of the player, etc. It is introduced to decrease the repetition code.
 */
@FunctionalInterface
public interface Operation {

    /**
     * Commences the commands.
     * 
     * @param oas optional arguments passed in the string representation.
     */
    public void commence(final String... oas);

}
