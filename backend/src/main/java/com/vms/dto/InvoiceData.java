class InvoiceData{
    String invoiceNumber;
    String invoiceDate; 
    String customerName;
    String customerAddress;
    String customerEmail;
    String customerPhone;           
    String itemDescription;
    int quantity;
    double unitPrice;
    double totalAmount;

    public InvoiceData(String invoiceNumber, String invoiceDate, String customerName, String customerAddress,
            String customerEmail, String customerPhone, String itemDescription, int quantity, double unitPrice,
            double totalAmount) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }   

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }


    public String getCustomerAddress() {
        return customerAddress;
    }

}