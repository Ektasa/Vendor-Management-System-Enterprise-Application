
@Repository
public interface InvoiceRepository implements JpaRepository<Invoice, Long> {
    public InvoiceData findByInvoiceNumber(String invoiceNumber);
    
}
    