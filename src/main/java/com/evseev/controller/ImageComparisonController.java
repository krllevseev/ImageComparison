package com.evseev.controller;


import com.evseev.imageProcessing.ImageComparator;
import com.evseev.imageProcessing.ImageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ImageComparisonController {


    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String getStartPage() {
        return "index";
    }
    
    
    @RequestMapping(method = RequestMethod.POST, value = "/")
    public String handleImageUpload(@RequestParam("img") MultipartFile firstImage,
                                   @RequestParam("img2") MultipartFile secondImage,
                                   RedirectAttributes redirectAttributes) {
    
        try {
            if (ImageUtils.validate(firstImage, secondImage)) {
                ImageComparator imageComparator = new ImageComparator(ImageUtils.loadImage(firstImage),
                        ImageUtils.loadImage(secondImage));
                imageComparator.compare();
                redirectAttributes.addFlashAttribute("message", "Images were compared");
                redirectAttributes.addFlashAttribute("success", true);
            }
            else {
                redirectAttributes.addFlashAttribute("message", "Images cannot be empty and must have .jpg extension.");
            }    
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "An error occured. Please try again later");
        }
        
        return "redirect:/";
    }


}
    
