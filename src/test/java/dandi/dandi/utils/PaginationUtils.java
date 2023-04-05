package dandi.dandi.utils;

import static org.springframework.data.domain.Sort.Direction.DESC;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PaginationUtils {

    public static final Pageable CREATED_AT_DESC_TEST_SIZE_PAGEABLE = PageRequest.of(0, 10, DESC, "createdAt");
}
