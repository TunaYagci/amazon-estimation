# Estimation API

A search-volume estimation by using Amazon's search API.

## 1. Algorithm

### Scoring
* Amazon's search API returns a list of top 10 keywords by their search volume.
* To score any keyword from 0 to 100, I've simply ranked keywords by their "first letter encounter".
* Since the API only offers "keyword" and "a set of results", the only thing you can do to reduce this **set** is
comparing them letter by letter.
* To give an example, if you search "i" on Amazon.com, it autocompletes it to "iphone charger".
* That means including of all keywords that starts with "**i**", "iphone charger" has a greater search-volume 
than other keywords that starts with **i**.

### Searching
* Because API had to deliver a score under 10 seconds, I needed a constant algorithm.
* Since searching every letter would take so much time for long keywords, I've applied binary search.
* So every keyword can be completed on O(log n) (where n is the number of letters in a keyword)
* An example of algorithm: 
* Searching for "iphone charger" would start as "iphone" -> "iph" -> "ip" -> "i"
* Single letters are scored as `100`.

## 2. Assumptions

* Since there is no way to compare search volumes of keywords that starting with **different** letters,
I've assumed that every starting letter is searched equally.
* If a keyword comes up in its first letter, this is a score of 100.
* If keyword doesn't come up at all, it is 0.
* Since there is also no way of comparing the score volume of `n` count of letter results with `m` count of letter results, 
I've multiplied every letter by `3`. 
* I've assumed all keywords are equally searched if they come up in the same suggestions.

## 3. The Hint

* I think whether the hint is true or not, it's not going to affect the output much. So I've assumed hint is correct.
* The bigger concern here is comparing the different sized keywords, rather than comparing equally-sized keywords.

## 4. How precise is the outcome

* It's not really precise.
* It can only be used to **compare keywords with same prefixes**, such as saying 
"iphone charger is definitely searched more than iphone 7", because they are both starting with **iphone**.
* Also this API is not documented via Amazon, so I wouldn't trust this outcome.


## 5. Docs

Please see Java interfaces for detailed explanation of what every service does.
