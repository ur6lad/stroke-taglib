# Stroke Taglib

The `stroke-taglib` is a JSP tag library. It makes tracking with [SiteCatalyst][adobe-analytics] easier.

The library makes data gathering less painfull. You can add SiteCatalyst's variable in any part of your JSP page(s) or even in a controller. Then you can populate the `s` object somewhere in the end of the page.

It uses the embeded client-side [Analytics JavaScript Tagging][s_code] code from the `s_code.js`.

[![Build Status](https://drone.io/bitbucket.org/ur6lad/stroke-taglib/status.png)](https://drone.io/bitbucket.org/ur6lad/stroke-taglib/latest)  
[![codecov.io](http://codecov.io/bitbucket/ur6lad/stroke-taglib/coverage.svg?branch=default)](http://codecov.io/bitbucket/ur6lad/stroke-taglib?branch=default)  
[![Flattr this](https://button.flattr.com/flattr-badge-large.png)](https://flattr.com/submit/auto?user_id=ur6lad&url=https%3A%2F%2Fbitbucket.org%2Fur6lad%2Fstroke-taglib&title=Adobe%20Analytics%20Taglib&tags=java,JSP,web%20analytcis&category=software&language=en_GB&description=Adobe%20Analytics%20Taglib%20is%20a%20JSP%20tag%20library%20used%20to%20collect%20data%20for%20Adobe%20Analytics%20was%20earlier%20known%20as%20Omniture%20SiteCatalyst.)

## What does it provide?

* Over 300 simple JSP tags for conversion, traffic, hierarcy and list variables, events and standard variables.
* Advanced JSP tags if you need more than 100 events or more than 75 conversion or traffic variables.
* Human readable JSP code: forget about bean and scriptlets.

## Usage

`stroke-taglib` is licensed under the Apache License v2.0

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)

### Add the Maven dependency

	<dependency>
		<groupId>ua.co.ur6lad</groupId>
		<artifactId>stroke-taglib</artifactId>
		<version>1.0.0</version>
	</dependency>

### Declare in JSP

	<%@ taglib uri='http://ur6lad.co.ua/stroke-taglib' prefix ='s' %>

### Examples

See snippets below:

* [Simple tags](https://snipt.net/ur6lad/stroke-taglib-simple-tags/)
* [Advanced tags](https://snipt.net/ur6lad/stroke-taglib-advanced-tags/)
* [Add a simple product](https://snipt.net/ur6lad/stroke-taglib-product/)
* [Add a product with events and variables](https://snipt.net/ur6lad/stroke-taglib-product-and-variables/)
* [Custom beans](https://snipt.net/ur6lad/stroke-taglib-custom-bean/)
* [Take down data](https://snipt.net/ur6lad/stroke-taglib-take-down/)

## Demo application

Feel free to [checkout][stroke-taglib-demo-checkout] or [download][stroke-taglib-demo-download] the demo web-application.

[![Download](https://img.shields.io/bintray/v/ur6lad/maven/stroke-taglib-demo.svg)](https://bintray.com/ur6lad/maven/stroke-taglib-demo/_latestVersion)

[adobe-analytics]: http://www.adobe.com/marketing-cloud/web-analytics.html "Adobe Analytics (Omniture SiteCatalyst)"
[s_code]: https://marketing.adobe.com/resources/help/en_US/sc/implement/appmeasure_mjs_pagecode.html
[stroke-taglib-demo-checkout]: https://bitbucket.org/ur6lad/stroke-taglib-demo
[stroke-taglib-demo-download]: https://bintray.com/ur6lad/maven/stroke-taglib-demo/view#files