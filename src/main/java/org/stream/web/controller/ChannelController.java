package org.stream.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.stream.auth.Principal;
import org.stream.entity.ChannelBean;
import org.stream.service.channel.IChannelService;
import org.stream.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;

/**
 * 频道管理
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/6/23
 * Time: 22:03
 */
@Controller
@RequestMapping("/adm/channel")
public class ChannelController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ChannelController.class);

    @Autowired
    private IChannelService channelService;

    @RequestMapping(value = "/addChannel", method = RequestMethod.GET)
    public ModelAndView addChannel(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/channel/addChannel");
        return modelAndView;
    }

    @RequestMapping(value = "/doAddChannel", method = RequestMethod.POST)
    public ModelAndView doAddChannel(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            Principal principal = this.getLoginPrincipal(request);
            if (principal == null) {
                ResponseUtil.handleLongin(modelAndView);
                return modelAndView;
            }

            String name = ServletRequestUtils.getStringParameter(request, "name", "");
            String dir = ServletRequestUtils.getStringParameter(request, "dir", "");

            ChannelBean channelBean = new ChannelBean();
            {
                channelBean.setName(name);
                channelBean.setDir(dir);
                channelBean.setCreateTime(new Timestamp(System.currentTimeMillis()));
            }
            channelService.addChanel(channelBean);
        } catch (Exception e) {
            log.info("Controller doAddChannel exception", e);
        }
        modelAndView.setViewName("redirect:/adm/channel/channelList");
        return modelAndView;
    }


    @RequestMapping(value = "/channelList", method = RequestMethod.GET)
    public ModelAndView channelList(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/channel/channelList");
        try {
            Principal principal = this.getLoginPrincipal(request);
            if (principal == null) {
                ResponseUtil.handleLongin(modelAndView);
                return modelAndView;
            }
            List<ChannelBean> channelList = channelService.getAllChannel();
            modelAndView.addObject("channelList", channelList);
        } catch (Exception e) {
            log.info("Controller channelList exception", e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/delChannel", method = RequestMethod.GET)
    public ModelAndView delChannel(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/adm/channel/channelList");
        try {
            Principal principal = this.getLoginPrincipal(request);
            if (principal == null) {
                ResponseUtil.handleLongin(modelAndView);
                return modelAndView;
            }
            long id = ServletRequestUtils.getLongParameter(request, "id", -1);
            channelService.deleteChannel(id);
        } catch (Exception e) {
            log.info("Controller delChannel exception", e);
        }
        return modelAndView;
    }


    @RequestMapping(value = "/toModifyChannel", method = RequestMethod.GET)
    public ModelAndView toModifyChannel(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/channel/modifyChannel");
        try {
            Principal principal = this.getLoginPrincipal(request);
            if (principal == null) {
                ResponseUtil.handleLongin(modelAndView);
                return modelAndView;
            }
            long id = ServletRequestUtils.getLongParameter(request, "id", -1);
            ChannelBean channelBean = channelService.getChannel(id);
            if (channelBean != null) {
                modelAndView.addObject("channel", channelBean);
            } else {
                modelAndView.addObject("error", "操作出问题了!");
                modelAndView.setViewName("/common/error");
            }
        } catch (Exception e) {
            log.info("Controller toModifyChannel exception", e);
        }
        return modelAndView;
    }


    @RequestMapping(value = "/doModifyChannel", method = RequestMethod.POST)
    public ModelAndView doModifyChannel(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/adm/channel/channelList");
        try {
            Principal principal = this.getLoginPrincipal(request);
            if (principal == null) {
                ResponseUtil.handleLongin(modelAndView);
                return modelAndView;
            }
            long id = ServletRequestUtils.getLongParameter(request, "id", -1);
            String name = ServletRequestUtils.getStringParameter(request, "name", "");
            String dir = ServletRequestUtils.getStringParameter(request, "dir", "");

            ChannelBean channelBean = new ChannelBean();
            {
                channelBean.setId(id);
                channelBean.setName(name);
                channelBean.setDir(dir);
            }
            channelService.updateChannel(channelBean);
        } catch (Exception e) {
            log.info("Controller doModifyChannel exception", e);
        }
        return modelAndView;
    }
}
