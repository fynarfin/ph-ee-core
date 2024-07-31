package org.mifos.connector.mockpaymentschema.schema;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BatchDetailResponse {

    private List<Transfer> content;

    private org.mifos.connector.mockpaymentschema.schema.PageableDTO pageable;

    private Long totalPages;

    private Long totalElements;

    private boolean last;

    private boolean first;

    private org.mifos.connector.mockpaymentschema.schema.SortDTO sort;

    private Long numberOfElements;

    private Long size;

    private Long number;

    private boolean empty;

}
