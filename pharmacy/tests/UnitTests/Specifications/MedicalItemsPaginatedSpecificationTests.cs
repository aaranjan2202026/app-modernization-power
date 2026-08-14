using System.Linq;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Specifications;
using Xunit;

namespace PharmacyNetwork.UnitTests.Specifications;

/// <summary>
/// Covers the filter expression and paging state of MedicalItemsPaginatedSpecification.
/// The Criteria expression is compiled and evaluated against in-memory objects, so
/// these tests need no database.
/// </summary>
public class MedicalItemsPaginatedSpecificationTests
{
    private static MedicalItem Item(int medItemId, int categId, int firmId) => new()
    {
        MedItemId = medItemId,
        CategId = categId,
        FirmId = firmId,
        MedItemName = $"Item-{medItemId}",
        MedItemPrice = 10m
    };

    private static readonly MedicalItem[] Sample =
    [
        Item(1, categId: 100, firmId: 200),
        Item(2, categId: 100, firmId: 201),
        Item(3, categId: 101, firmId: 200),
        Item(4, categId: 101, firmId: 201)
    ];

    [Fact]
    public void ApplyPaging_SetsSkipTakeAndEnablesPaging()
    {
        var spec = new MedicalItemsPaginatedSpecification(skip: 10, take: 5, categId: null, firmId: null);

        Assert.Equal(10, spec.Skip);
        Assert.Equal(5, spec.Take);
        Assert.True(spec.IsPagingEnabled);
    }

    [Fact]
    public void Criteria_WithNoFilters_MatchesEveryItem()
    {
        var spec = new MedicalItemsPaginatedSpecification(0, 10, categId: null, firmId: null);
        var predicate = spec.Criteria.Compile();

        Assert.All(Sample, item => Assert.True(predicate(item)));
    }

    [Fact]
    public void Criteria_FiltersByCategoryOnly()
    {
        var spec = new MedicalItemsPaginatedSpecification(0, 10, categId: 100, firmId: null);
        var predicate = spec.Criteria.Compile();

        var matched = Sample.Where(predicate).Select(i => i.MedItemId).ToArray();

        Assert.Equal([1, 2], matched);
    }

    [Fact]
    public void Criteria_FiltersByFirmOnly()
    {
        var spec = new MedicalItemsPaginatedSpecification(0, 10, categId: null, firmId: 200);
        var predicate = spec.Criteria.Compile();

        var matched = Sample.Where(predicate).Select(i => i.MedItemId).ToArray();

        Assert.Equal([1, 3], matched);
    }

    [Fact]
    public void Criteria_FiltersByCategoryAndFirmTogether()
    {
        var spec = new MedicalItemsPaginatedSpecification(0, 10, categId: 101, firmId: 201);
        var predicate = spec.Criteria.Compile();

        var matched = Sample.Where(predicate).Select(i => i.MedItemId).ToArray();

        Assert.Equal([4], matched);
    }

    [Fact]
    public void Criteria_WithNoMatches_ReturnsEmpty()
    {
        var spec = new MedicalItemsPaginatedSpecification(0, 10, categId: 999, firmId: 999);
        var predicate = spec.Criteria.Compile();

        Assert.DoesNotContain(Sample, item => predicate(item));
    }

    [Fact]
    public void Specification_WithoutPaging_LeavesPagingDisabled()
    {
        // MedicalItemsSpecification does not call ApplyPaging
        var spec = new MedicalItemsSpecification(categId: null, firmId: null);

        Assert.False(spec.IsPagingEnabled);
        Assert.Equal(0, spec.Skip);
        Assert.Equal(0, spec.Take);
    }
}
