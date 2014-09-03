/* Copyright 2014 the original author or authors:
*
* Rodolfo Castellanos (rodolfojcj at yahoo dot com)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.opensinergia

/*
 * Taglib for AdminLTE related tasks
 *
 * @author Rodolfo Castellanos
 */
class AdminLTETagLib {
    static namespace = 'altt'

    // works! with the default html tags used by the nav.menu tag library
    // sadly, no customizations at all :( so no exactly what we need for AdminLTE
    //def sidebarMenu = {attrs, body ->
    //    out << nav.menu(attrs + [class: "sidebar-menu"])
    //}

    def sidebarMenu = {attrs, body ->
        // see line 158 of NavigationTagLib.groovy. That's a closure blessing!
        // out << body([item:n, linkArgs:linkArgs, active:active, enabled:enabled])
        out << nav.menu(attrs + [class: "sidebar-menu", custom: true], {menuAttrs ->
            if (!menuAttrs.item?.children || menuAttrs.item.children.size() == 0) {
                out << '<li' + (menuAttrs.active ? ' class="active"' : '') + '>'
                out << p.callTag(tag: 'g:link', attrs: menuAttrs.linkArgs, {
                    out << '<i class="fa' + (menuAttrs.item.data?.faIcon ? ' ' + menuAttrs.item.data.faIcon : '') + '"></i>'
                    out << "<span>" + nav.title(menuAttrs) + "</span>"
                })
                out << "</li>"
            }
            else if (menuAttrs.item?.children && menuAttrs.item?.children.size() >= 1) {
                out << '<li class="treeview' + (menuAttrs.active ? ' active' : '') + '">'
                out << '<a href="#">'
                out << '<i class="fa' + (menuAttrs.item.data?.faIcon ? ' ' + menuAttrs.item.data.faIcon : '') + '"></i>'
                out << ' <span>' + nav.title(menuAttrs) + '</span>'
                out << '<i class="fa fa-angle-left pull-right"></i>'
                out << '</a>'
                out << '<ul class="treeview-menu">'
                menuAttrs.item.children.each {item ->
                    // only a max of 2 levels depth hierarchy for the way AdminLTE theme works
                    // so, no recursive handling for deeper/nested items
                    out << '<li>'
                        // clone linkArgs! see line 154 of NavigationTagLib.groovy
                        out << p.callTag(tag: 'g:link', attrs: new HashMap(item.linkArgs), {
                            out << '<i class="fa fa-angle-double-right"></i>'
                            //out << nav.title(item) // it did not work because this is a leaf node
                            out << item.titleDefault // TODO: see line 290 of NavigationTagLib.groovy using g:message
                        })
                    out << '</li>'
                }
                out << '</ul>' // </ul> with "treeview-menu" class
                out << '</li>' // </ul> with "treeview" class
            }
        })
    }

    def sidebarForm = {
      out << '<form action="#" method="get" class="sidebar-form"><div class="input-group"><input name="q" class="form-control" placeholder="Search..." type="text"><span class="input-group-btn"><button type="submit" name="seach" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i></button></span></div></form>'
    }

    def sidebar = {attrs, body ->
        // by default search form will be included, even when not specified
        def withSearchForm = true && (attrs.withSearchForm == 'true' || attrs.withSearchForm.is(Boolean.TRUE) || attrs.withSearchForm == null)
        out << '<section class="sidebar">'
        if (withSearchForm)
            out << altt.sidebarForm()
        out << altt.sidebarMenu(attrs, body)
        out << '</section>'
    }
}