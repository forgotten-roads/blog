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

TBD


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
