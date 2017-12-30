# forgotten.roads.mx Blog

*The Forgotten Roads Blog for Motorcycle Excursionists*

Visit: http://forgotten.roads.mx/blog/


## Prerequisites

* GNU `make`
* The `lessc` CSS compiler (which requires having `npm` installed)

Set the `PATH` to include the project's executable and setup auto-completion:

```bash
$ export PATH=$PATH:`pwd`/bin
$ source dev-resources/shell/frmx-bash-autocompletion
```


## Creating Post Stubs

```bash
$ frmx new post md
```

or, for example,

```bash
$ frmx new post rfc5322
```

For more options see `frmx new post help`.


## Generating Static Files

```bash
$ frmx gen
```

If you'd like to run a dev web server with the generated content served at the
doc root, you can use this `make` target:

```
$ make blog-dev-gen
```


## Checking Metadata and Content

Get a listing of all posts by content file, title, and subtitle:

```basg
$ frmx show posts
```
```
./posts/2016-07/31-114941/content.rfc5322 - Knucklehead at Sturgis: Excursionists: Arctic Adventures
./posts/2016-08/25-174345/content.rfc5322 - Nation of Patriots 2016: Passing of the Flag in Sioux Falls, SD
./posts/2016-09/19-221343/content.rfc5322 - The Beginning of a New Project: A Resource for Motorcycle Excursionists
./posts/2017-06/07-165957/content.rfc5322 - Nation of Patriots 2017: Passing of the Flag in Sioux Falls, SD and Norfolk, NE
./posts/2017-06/15-223122/content.rfc5322 - Dam Ride 2017: Day 1
```


## Developing Content

Start up the REPL:

```
$ lein repl
```

Regenerate the content and start the local dev server:

```clj
(core/generate+web)
```

Edit files, reload the Clojure namespaces, and regenerate the content:

```clj
(reload)
(core/generate)
```

the FRMX Blog (via Dragon) also supports the following custom Selmer tags:

* `flickr-img`
* `panel`, `panel-table`, `panel-row`, `panel-img`, and `panel-body`

These are covered in more detail in the following subsections.


### `flickr-img`

Single, large image:

```clj
{% flickr-img :photo-id 35731320501
              :album-id 72157683434776573
              :user forgottenroadsmx
              :width 2048 %}
```

Two images, side-by side taking up content width:

```clj
{% flickr-img :photo-id 35490469260
              :album-id 72157683434776573
              :user forgottenroadsmx
              :width 371 %}
{% flickr-img :photo-id 36417440705
              :album-id 72157683434776573
              :user forgottenroadsmx
              :width 371 %}
```

Three images, side-by side taking up content width:

```clj
{% flickr-img :photo-id 37788179682
              :album-id 72157684801453550
              :user forgottenroadsmx
              :width 244 %}
{% flickr-img :photo-id 36221810812
              :album-id 72157684801453550
              :user forgottenroadsmx
              :width 244 %}
{% flickr-img :photo-id 35581714293
              :album-id 72157684801453550
              :user forgottenroadsmx
              :width 244 %}
```

Four images, side-by side taking up content width:

```clj
{% flickr-img :photo-id 36220806662
              :album-id 72157683434776573
              :user forgottenroadsmx
              :width 182 %}
{% flickr-img :photo-id 36344127846
              :album-id 72157683434776573
              :user forgottenroadsmx
              :width 182 %}
{% flickr-img :photo-id 36344123656
              :album-id 72157683434776573
              :user forgottenroadsmx
              :width 182 %}
{% flickr-img :photo-id 36344122326
              :album-id 72157683434776573
              :user forgottenroadsmx
              :width 182 %}
```


### `panel` Tags

Here's an example of all the panel tags in action:

```clj
{% panel :class "route-summary" %}
   {% panel-table :title "Day Summary" %}
      {% panel-row :title "Start:" %}
        [Belle Fourche, SD](https://en.wikipedia.org/wiki/Belle_Fourche,_South_Dakota) 6:19 am CDT
      {% end-panel-row %}
      {% panel-row :title "End:" %}
        [Red Lodge, MT](https://en.wikipedia.org/wiki/Red_Lodge,_Montana) 4:39 pm CDT
      {% end-panel-row %}
      {% panel-row :title "Distance:" %}
        374 mi (602 km)
      {% end-panel-row %}
      {% panel-row :title "Food:" %}
        Breakfast @ Riverside Campground in Belle Fourche, SD

        Second Breakfast @ [Cashway Cafe](https://www.tripadvisor.com/Restaurant_Review-g45098-d4914167-Reviews-Cashway_Cafe-Broadus_Montana.html) in Broadus, MT

        Lunch @ [Thai Cuisine](https://www.tripadvisor.com/Restaurant_Review-g45086-d3266456-Reviews-Cham_Thai_Cuisine-Billings_Montana.html) in Billings, MT

        Dinner @ [Red Lodge Pizza Co.](http://www.redlodgerestaurants.com/red-lodge-pizza-co/) in Red Lodge, MT
      {% end-panel-row %}
      {% panel-row :title "Slept:" %}
        [Rattin Campground](https://www.fs.usda.gov/recarea/custergallatin/recarea/?recid=60831) (tent)
      {% end-panel-row %}
      {% panel-row :title "Photo Album:" %}
        [Beartooth Run 2017 - Day 2](https://www.flickr.com/photos/forgottenroadsmx/albums/72157683434776573) (Flickr)
      {% end-panel-row %}
   {% end-panel-table %}
   {% panel-img :title "Route Map"
                :src "https://farm5.staticflickr.com/4436/36256381541_2b06e22b88_k_d.jpg" %}
   {% panel-body :title "Beartooth Run, 2017 Series Links" %}
     * [Prologue](/blog/archives/2017-06/29-115623/beartooth-run-fargo-h-o-g-.html)
     * [Day 1](/blog/archives/2017-07/06-193659/beartooth-run-2017.html)
     * [Day 2](/blog/archives/2017-07/07-212540/beartooth-run-2017.html)
     * [Day 3](/blog/archives/2017-07/08-220414/beartooth-run-2017.html)
     * [Red Lodge, MT](/blog/archives/2017-07/13-154024/beartooth-run-2017.html)
     * [Day 4](/blog/archives/2017-07/14-210408/beartooth-run-2017.html)
     * [Day 5](/blog/archives/2017-07/15-221856/beartooth-run-2017.html)
     * [Day 6](/blog/archives/2017-07/16-192353/beartooth-run-2017.html)
   {% end-panel-body %}
{% end-panel %}
```

You can see that rendered on the
[Day 2](http://forgotten.roads.mx/blog/archives/2017-07/07-212540/beartooth-run-2017.html)
post of the Beartooth Run trip.

## Publishing Content

To (re-)publish all content to AWS:

```
$ make publish-aws
```

To sync only content that has changed (includes timestamp-only changes):

```
$ make sync-aws
```

Additional publishing `make` targets:

* To only publish content that is currently in a "modified" state in `git` you
  can use `make publish-aws-modified`
* To publish the files from the last commit you can use
 `make publish-aws-committed`
