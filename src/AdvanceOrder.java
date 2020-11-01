import java.util.Map;

public class AdvanceOrder extends Order{
    private String customerPhone;
    /**
     * Constructor for objects of class AdvanceOrder
     * have a non-parameterised (“default”) constructor
     */
    public AdvanceOrder() {
        this.customerPhone = "";
    }

    /**
     * Constructor for objects of class AdvanceOrder
     * have a parameterised constructor
     */

    public AdvanceOrder(String orderDate, String orderId, String orderTime, String orderStatus,
                        Map<FoodItem, Integer> quantity, Double totalCost, String lastModifiedDate,
                        String lastModifiedTime, String nameOfCustomer, String lastModifiedBy, String customerPhone) {
        super(orderDate, orderId, orderTime, orderStatus, quantity, totalCost, lastModifiedDate, lastModifiedTime,
                nameOfCustomer, lastModifiedBy);
        this.customerPhone = customerPhone;
    }

    /**
     * Get method
     * @return the customerPhone
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * Mutator method
     * @param   customerPhone   the new customerPhone in the advance order
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

}
