import java.util.Map;

public class AdvanceOrder extends Order{
    private String customerPhone;

    public AdvanceOrder() {
        this.customerPhone = "";
    }

    public AdvanceOrder(String orderDate, String orderId, String orderTime, String orderStatus,
                        Map<FoodItem, Integer> quantity, Double totalCost, String lastModifiedDate,
                        String lastModifiedTime, String nameOfCustomer, String lastModifiedBy, String customerPhone) {
        super(orderDate, orderId, orderTime, orderStatus, quantity, totalCost, lastModifiedDate, lastModifiedTime,
                nameOfCustomer, lastModifiedBy);
        this.customerPhone = customerPhone;
    }


    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
