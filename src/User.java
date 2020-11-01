/**
 * @version: V1.0
 * @author: Team_04
 * @className: User
 * @description: Entity Class for User
 * @data: 2020-11-1 9:00
 **/
public class User {

	protected Integer userId;
	protected String userName;
	protected String userEmail;
	protected String userPassword;
	protected String employeeTaxFileNumber;
	protected String employeeResidentialAddress;
	protected String userContactNumber;
	protected String userType;
	protected String userStatus;

	/**
	 * Constructor for objects of class User
	 * have a non-parameterised (“default”) constructor
	 */
	public User() {
		super();
		this.userId = 0;
		this.userName = "";
		this.userEmail = "";
		this.userPassword = "";
		this.employeeTaxFileNumber = "";
		this.employeeResidentialAddress = "";
		this.userContactNumber = "";
		this.userType = "";
		this.userStatus = "";
	}

	/**
	 * Constructor for objects of class User
	 * have a parameterised constructor
	 */
	public User(Integer userId, String userName, String userEmail, String userPassword, String employeeTaxFileNumber,
			String employeeResidentialAddress, String userContactNumber, String userType, String userStatus) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.employeeTaxFileNumber = employeeTaxFileNumber;
		this.employeeResidentialAddress = employeeResidentialAddress;
		this.userContactNumber = userContactNumber;
		this.userType = userType;
		this.userStatus = userStatus;
	}

	/**
	 * Get method
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Get method
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Get method
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * Get method
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * Get method
	 * @return the employeeTaxFileNumber
	 */
	public String getEmployeeTaxFileNumber() {
		return employeeTaxFileNumber;
	}

	/**
	 * Get method
	 * @return the employeeResidentialAddress
	 */
	public String getEmployeeResidentialAddress() {
		return employeeResidentialAddress;
	}

	/**
	 * Get method
	 * @return the userContactNumber
	 */
	public String getUserContactNumber() {
		return userContactNumber;
	}

	/**
	 * Get method
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * Get method
	 * @return the userStatus
	 */
	public String getUserStatus() {
		return userStatus;
	}

	/**
	 * Mutator method
	 * @param   userId  the new user ID in the User
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * Mutator method
	 * @param   userName   the new user name in the User
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Mutator method
	 * @param   userEmail   the new user email in the User
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * Mutator method
	 * @param   userPassword  the new user password in the User
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * Mutator method
	 * @param   employeeTaxFileNumber   the new employeeTaxFileNumber in the User
	 */
	public void setEmployeeTaxFileNumber(String employeeTaxFileNumber) {
		this.employeeTaxFileNumber = employeeTaxFileNumber;
	}

	/**
	 * Mutator method
	 * @param   employeeResidentialAddress   the new employeeResidentialAddress in the User
	 */
	public void setEmployeeResidentialAddress(String employeeResidentialAddress) {
		this.employeeResidentialAddress = employeeResidentialAddress;
	}

	/**
	 * Mutator method
	 * @param   userContactNumber   the new user contact number in the User
	 */
	public void setUserContactNumber(String userContactNumber) {
		this.userContactNumber = userContactNumber;
	}

	/**
	 * Mutator method
	 * @param   userType   the new user type in the User
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * Mutator method
	 * @param   userStatus   the new user status in the User
	 */
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

}
