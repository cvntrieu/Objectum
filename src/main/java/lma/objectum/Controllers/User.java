
package lma.objectum.Controllers;

public class User {

    private String username;
    private String firstname;
    private String lastname;
    private String role;

    /**
     * Default constr.
     */
    public User() {}

    /**
     * Constr.
     *
     * @param username account
     * @param firstname name
     * @param lastname name
     * @param role admin or member
     */
    public User(String username, String firstname, String lastname, String role) {

        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
