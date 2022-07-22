package com.mugencai.myblog.web.admin;

import com.mugencai.myblog.pojo.Type;
import com.mugencai.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    private TypeService typeService;

    //分页查询的控制器
    @GetMapping("/types")
    public String types(@PageableDefault(value = 5,sort = {"id"},direction = Sort.Direction.DESC)
                        Pageable pageable,Model model) {
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

    //返回新增页面
    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }


    //修改后返回新增页面
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute( "type", typeService.getType(id));
        return "admin/types-input";
    }

    //保存提交结果
    @PostMapping("/types")
    public String post(Type type, BindingResult result, RedirectAttributes attributes) {

        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null) {
            result.rejectValue("name", "nameError", "不能添加重复分类");
        }

        Type t = typeService.saveType(type);
        if (t == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/types";
    }


    //修改种类名称
    @PostMapping("/types/{id}")
    public String post(Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes) {

        Type type2 = typeService.getTypeByName(type.getName());
        if (type2 != null) {
            result.rejectValue("name", "nameError", "不能添加重复分类");
        }

        Type t = typeService.updateType(id, type);
        if (t == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/types";
    }

    //删除type
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }

}
