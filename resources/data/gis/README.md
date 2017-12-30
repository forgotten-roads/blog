# Adding New GIS Data

When adding new `.kml` data for the maps section of the Forgotten Roads blog,
here are the steps you'll want to follow:

1. Obtain your `.kml` file:
   * Download from Spotwalla
     ([tracks](https://spotwalla.com/tracks.php),
     [trips](https://spotwalla.com/trips.php), or both)
   * Create a route at
     [OpenRouteService](https://openrouteservice.org/directions) and download
     from there when done
2. Create an appropriate directory (if a new topic) in
  `resources/data/gis`, be sure the `.kml` file is named descriptively,
  and move it to the appropriate directory.
3. Add the FRMX route style to the `.kml` file:
   * As a child element of the `Document` tag (sibling to the `Placemark`
     tag), add the following:
```html
    <Style id="lineColor">
      <LineStyle>
        <color>551400AA</color>
        <width>8</width>
      </LineStyle>
    </Style>
```
   * As a child element of the `Placemark` tag, add the following:
```html
<styleUrl>#lineColor</styleUrl>
```
4. Add and commit the file using `git`.
5. Push the data with `make publish-data-aws`.
6. Regenerate the site content (to create the new `.html` pages for the
   added GIS data as well as to update the maps index page to include links
   to those new pages).
7. Publish the updated site content with `make sync-aws`.
8. Optional: If you make any mistakes with the data or pages and need to
   re-publish, you'll want to bust the Google Maps JavaScript API cache by
   incrementing the version in the URL; to do this, increment the `revision`
   constant defined at the top of `mx.roads.forgotten.blog.maps`, and then
   regenerate the site content.
