package gregdev.spring_enterprises.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gregdev.spring_enterprises.model.EnterpriseModel;
import gregdev.spring_enterprises.service.EnterpriseService;
import gregdev.spring_enterprises.utils.Utilities;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class EnterpriseController {

    private final EnterpriseService enterpriseService;
    private static final String RECORD_NOT_FOUND = "Record not found";
    private static final String INTERNAL_SERVER_ERROR = "An internal server error ocurred.";

    private static final String SUCCESS_CODE = "AC200";
    private static final String NOTFOUND_CODE = "AC404";
    private static final String BADREQUEST_CODE = "AC400";
    private static final String INTERNAL_SERVER_ERROR_CODE = "AC500";

    private static final String LOG_NOT_FOUND = "EntityNotFoundException: {}";
    private static final String LOG_EXCEPTION = "Exception: {}";

    EnterpriseController(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    // Test
    @GetMapping("/test")
    public String test() {
        log.info("Test");
        return "Ok";
    }

    // GetAll
    @GetMapping("/enterprise")
    public List<EnterpriseModel> getAll() {
        log.info("Fetching all enterprises");
        List<EnterpriseModel> enterprises = this.enterpriseService.getAll();
        log.info("Fetched {} enterprises", enterprises.size());
        return enterprises;
    }

    // GetById
    @GetMapping("enterprise/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Integer id) {
        log.info("Fetching enterprise with id: {}", id);
        try {
            EnterpriseModel enterprise = this.enterpriseService.findById(id);
            if (enterprise != null) {
                log.info("Enterprise found: {}", enterprise);
                return ResponseEntity.ok(enterprise);
            } else {
                log.warn("Enterprise with id {} not found", id);
                return Utilities.generateResponse(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND, NOTFOUND_CODE);
            }
        } catch (EntityNotFoundException e) {
            log.error(LOG_NOT_FOUND, e.getMessage());
            return Utilities.generateResponse(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND, NOTFOUND_CODE);
        } catch (Exception e) {
            log.error(LOG_EXCEPTION, e.getMessage());
            return Utilities.generateResponse(HttpStatus.BAD_GATEWAY, INTERNAL_SERVER_ERROR,
                    INTERNAL_SERVER_ERROR_CODE);
        }
    }

    // GetByUUID
    @GetMapping("enterprise/uuid/{uuid}")
    public ResponseEntity<Object> getByUuid(@PathVariable("uuid") String uuidStr) {
        log.info("Fetching enterprise with UUID: {}", uuidStr);
        try {
            UUID uuid = parseUuid(uuidStr);
            EnterpriseModel enterprise = this.enterpriseService.findByUuid(uuid);
            if (enterprise != null) {
                log.info("Enterprise found: {}", enterprise);
                return ResponseEntity.ok(enterprise);
            } else {
                log.warn("Enterprise with UUID {} not found", uuidStr);
                return Utilities.generateResponse(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND, NOTFOUND_CODE);
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", uuidStr);
            return Utilities.generateResponse(HttpStatus.BAD_REQUEST, "Invalid UUID format", BADREQUEST_CODE);
        } catch (EntityNotFoundException e) {
            log.error(LOG_NOT_FOUND, e.getMessage());
            return Utilities.generateResponse(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND, NOTFOUND_CODE);
        } catch (Exception e) {
            log.error(LOG_EXCEPTION, e.getMessage());
            return Utilities.generateResponse(HttpStatus.BAD_GATEWAY, INTERNAL_SERVER_ERROR,
                    INTERNAL_SERVER_ERROR_CODE);
        }
    }

    // Create
    @PostMapping("/enterprise")
    public ResponseEntity<Object> createEnterprise(@RequestBody EnterpriseModel request) {
        log.info("Creating new enterprise: {}", request);
        try {
            String razonSocial = request.getRazonSocial();
            String rfc = request.getRfc();
            String telefono = request.getTelefono();
            String contacto = request.getContacto();
            String correo = request.getCorreo();

            if (razonSocial != null && rfc != null && telefono != null && contacto != null && correo != null) {
                this.enterpriseService.save(request);
                log.info("Enterprise created successfully: {}", request);
                return Utilities.generateResponse(HttpStatus.OK, "Record created successfully", SUCCESS_CODE);
            }
            log.warn("Missing required fields in request: {}", request);
            return Utilities.generateResponse(HttpStatus.BAD_REQUEST, "All fields are required.", BADREQUEST_CODE);
        } catch (Exception e) {
            log.error(LOG_EXCEPTION, e.getMessage());
            return Utilities.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR,
                    INTERNAL_SERVER_ERROR_CODE);
        }
    }

    // Delete
    @DeleteMapping("/enterprise/{id}")
    public ResponseEntity<Object> deleteEnterprise(@PathVariable("id") Integer id) {
        log.info("Deleting enterprise with id: {}", id);
        try {
            EnterpriseModel enterprise = this.enterpriseService.findById(id);
            if (enterprise == null) {
                log.warn("Enterprise with id {} not found", id);
                return Utilities.generateResponse(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND, NOTFOUND_CODE);
            } else {
                this.enterpriseService.delete(id);
                log.info("Enterprise with id {} deleted successfully", id);
                return Utilities.generateResponse(HttpStatus.OK, "Record deleted successfully", SUCCESS_CODE);
            }
        } catch (EntityNotFoundException e) {
            log.error(LOG_NOT_FOUND, e.getMessage());
            return Utilities.generateResponse(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND, NOTFOUND_CODE);
        } catch (Exception e) {
            log.error(LOG_EXCEPTION, e.getMessage());
            return Utilities.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR,
                    INTERNAL_SERVER_ERROR_CODE);
        }
    }

    // Método para validar el UUID
    private UUID parseUuid(String uuidStr) {
        return UUID.fromString(uuidStr);
    }
}
