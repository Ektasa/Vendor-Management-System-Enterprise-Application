

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController{

    private final InvoiceService invoiceService;

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceData> getInvoiceById(@PathVariable Long id) {
        InvoiceData invoiceData = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoiceData);
    }

    @PostMapping
    public ResponseEntity<InvoiceData> createInvoice(@RequestBody InvoiceData invoicedata)
    {
        InvoiceData createdInvoice=invoiceService.createInvoice(invoicedata);
        return ResponseEntity.ok(createdInvoice);
    }

    @GetMapping("/all")
    public ResponseEntity<List<InvoiceData>> getAllInvoices()
    {
        List<InvoiceData> invoices=invoicesService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

}