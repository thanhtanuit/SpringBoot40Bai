package com.example.shop.web;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    // Dữ liệu tạm trong bộ nhớ — Bài 3 sẽ tách sang Repository, Bài 8 xuống database thật.
    private final List<Product> products = new CopyOnWriteArrayList<>();
    private final AtomicLong idSeq = new AtomicLong(0);
    @GetMapping()
    public List<Product> findAll(){
        return products;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return timTheoId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());   // 404
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product body) {
        // Bỏ qua id do client gửi lên — server là nơi duy nhất cấp id.
        Product moi = new Product(idSeq.incrementAndGet(), body.name(), body.price());
        products.add(moi);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(moi.id())
                .toUri();

        return ResponseEntity.created(location).body(moi);             // 201 + header Location
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product body) {
        int i = viTri(id);
        if (i < 0) return ResponseEntity.notFound().build();           // 404

        Product capNhat = new Product(id, body.name(), body.price());
        products.set(i, capNhat);                                      // record bất biến -> thay cả object
        return ResponseEntity.ok(capNhat);                             // 200
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        int i = viTri(id);
        if (i < 0) return ResponseEntity.notFound().build();           // 404

        products.remove(i);
        return ResponseEntity.noContent().build();                     // 204, không có body
    }

    private Optional<Product> timTheoId(Long id) {
        return products.stream().filter(p -> p.id().equals(id)).findFirst();
    }

    private int viTri(Long id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).id().equals(id)) return i;
        }
        return -1;
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<Product> updatePrice(@PathVariable Long id,
                                               @RequestBody Product.PriceUpdate body) {
        int i = viTri(id);
        if (i < 0) return ResponseEntity.notFound().build();

        Product cu = products.get(i);
        Product moi = new Product(cu.id(), cu.name(), body.price());   // giữ nguyên name
        products.set(i, moi);
        return ResponseEntity.ok(moi);
    }
}
