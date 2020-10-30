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

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getEmployeeTaxFileNumber() {
		return employeeTaxFileNumber;
	}

	public String getEmployeeResidentialAddress() {
		return employeeResidentialAddress;
	}

	public String getUserContactNumber() {
		return userContactNumber;
	}

	public String getUserType() {
		return userType;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setEmployeeTaxFileNumber(String employeeTaxFileNumber) {
		this.employeeTaxFileNumber = employeeTaxFileNumber;
	}

	public void setEmployeeResidentialAddress(String employeeResidentialAddress) {
		this.employeeResidentialAddress = employeeResidentialAddress;
	}

	public void setUserContactNumber(String userContactNumber) {
		this.userContactNumber = userContactNumber;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

}
