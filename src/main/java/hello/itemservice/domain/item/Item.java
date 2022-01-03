package hello.itemservice.domain.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Data 핵심 도메인 모델에 사용하지 않는 것을 권장.
//@RequiredArgsConstructor final 이나 NonNull 인 프로퍼티만 파라미터로 받는 생성자 자동 생성
@Getter
@Setter
public class Item {

    private Long id;
    private ItemParamDto itemParamDto;

    public Item() {
    }

    public Item(ItemParamDto itemParamDto) {
        this.itemParamDto = itemParamDto;
    }

}
