package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemParamDto;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final 인 프로퍼티로 생성자 만들어줌
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findByID(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                          @RequestParam int price,
                          @RequestParam Integer quantity,
                          Model model) {
        Item savedItem = itemRepository.save(new Item(new ItemParamDto(itemName, price, quantity)));
        model.addAttribute("item", savedItem);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") ItemParamDto itemParamDto,
                            Model model) {
        Item savedItem = itemRepository.save(new Item(itemParamDto));
        model.addAttribute("item", savedItem);
        Long savedItemId = savedItem.getId();
        return "redirect:/basic/items/" + savedItemId;
        // 웹 브라우저의 새로고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
        // 즉, POST 한 직후 새로고침을 하면 그 데이터로 다시 POST 를 날리게 된다.
        // 이 문제를 해결하려면 POST 후 뷰 템플릿을 호출하지 말고 리다이렉트를 해주면 된다.
        // 웹 브라우저 입장에서 리다이렉트는 화면을 실제 자기자신이 호출해서 이동한 것이기 때문에
        // 마지막에 호출한 내용이 [GET 리다이렉트 url] 이므로 문제가 해결된다.
        // PRG 패턴으로 부른다. (Post Redirect Get)
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute("item") Item item) {
        itemRepository.save(item);
        return "basic/item";
    }
    // paramDto 를 안 썼으면 사용할 수 있는 방법.
    // ModelAttribute 의 기능
    // ModelAttribute 로 지정한 객체를 자동으로 그 이름을 가지고 Model 에 넣어준다!

//    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }
    // v3 에 이어서... @ModelAttribute 생략도 가능

//    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
        // 그냥 + item.getId() 해버리면 url encoding 이 안됨
    }

    @PostMapping("/add")
    public String addItemV6(@ModelAttribute("item") ItemParamDto itemParamDto,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        Item savedItem = itemRepository.save(new Item(itemParamDto));
        model.addAttribute("item", savedItem);

        Long savedItemId = savedItem.getId();
        redirectAttributes.addAttribute("itemId", savedItem.getId()); // redirect url 에 {~~} 로 들어가거나
        redirectAttributes.addAttribute("status", true); // {~~~} 없으면 쿼리 파라미터로 넘어감
        // RedirectAttributes 사용하면 url encoding 자동으로 됨
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editItem(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findByID(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable Long itemId,
                           @RequestParam String itemName,
                           @RequestParam int price,
                           @RequestParam Integer quantity,
                           Model model) {
        itemRepository.update(itemId, new ItemParamDto(itemName, price, quantity));
        Item updatedItem = itemRepository.findByID(itemId);
        model.addAttribute("item", updatedItem);
        return "redirect:/basic/items/{itemId}";
        // {itemId} 와 같은 방식으로 redirect 시 PathVariable 사용 가능
    }

    /**
     * 테스트 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item(new ItemParamDto("itemA", 10000, 10)));
        itemRepository.save(new Item(new ItemParamDto("itemB", 20000, 20)));
    }
}
