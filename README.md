# Example

```bash
./compiledjar start=formation=2022-01-10T00:00:00 end=formation=2022-01-11T00:00:00
```

# Options

Options should be specified like this :  
`keyA=valueA keyB=valueB`

They should be separated by spaces and key / values must be separated by an equal sign, without any space.

## formations
Formation that you want to fetch from Celcat, separated by semicolons.  
Format: Separated by semicolons  
Example: `formation=IINE9CMA;MBEA7CMA`

Those formations can be found on the URL when you access the Celcat page after you have selected some groups.

## start
The date from which the events should be fetched  
Default : current date  
Format : yyyy-mm-ddThh:mm:ss  
Example : `start=2022-01-10T23:04:00`

## end
The date until which the events should be fetched  
Default : start  
Format : yyyy-mm-ddThh:mm:ss  
Example : `end=2022-01-10T23:04:00`

## pretty
Whether the JSON should be printed with proper indent, default = true  
Default : true  
Format : Boolean  
Example : `pretty=false`